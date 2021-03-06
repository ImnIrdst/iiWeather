package com.imn.iiweather.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.imn.iiweather.data.local.AppDatabase
import com.imn.iiweather.data.local.location.FusedLocationLiveData
import com.imn.iiweather.data.remote.weather.DefaultWeatherRemoteDataSource
import com.imn.iiweather.data.repository.location.DefaultLocationRepository
import com.imn.iiweather.data.repository.weather.DefaultWeatherRepository
import com.imn.iiweather.data.repository.weather.WeatherLocalDataSource
import com.imn.iiweather.data.repository.weather.WeatherRemoteDataSource
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.repository.LocationRepository
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.domain.utils.State
import com.imn.iiweather.ui.main.MainViewModelFactory
import com.imn.iiweather.utils.BuildUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object ServiceLocator {


    private val lock = Any()

    @Volatile
    var mainViewModelFactory: MainViewModelFactory? = null
        @VisibleForTesting set

    fun provideMainViewModelFactory(appContext: Context): MainViewModelFactory {
        synchronized(lock) {
            return mainViewModelFactory ?: MainViewModelFactory(
                provideWeatherRepository(appContext),
                provideLocationRepository(appContext)
            )
        }
    }

    @Volatile
    var locationRepository: LocationRepository? = null
        @VisibleForTesting set


    private fun provideLocationRepository(applicationContext: Context): LocationRepository {
        synchronized(lock) {
            return locationRepository ?: DefaultLocationRepository(
                provideLocationLiveData(applicationContext)
            )
        }
    }

    @Volatile
    var fusedLocationLiveData: LiveData<State<LocationModel>>? = null
        @VisibleForTesting set


    private fun provideLocationLiveData(context: Context): LiveData<State<LocationModel>> {
        synchronized(lock) {
            return fusedLocationLiveData ?: FusedLocationLiveData(context)
        }
    }

    @Volatile
    var weatherRepository: WeatherRepository? = null
        @VisibleForTesting set

    fun provideWeatherRepository(applicationContext: Context): WeatherRepository {
        synchronized(lock) {
            return weatherRepository ?: DefaultWeatherRepository(
                provideWeatherLocalDataSource(applicationContext),
                provideWeatherRemoteDataSource(),
            )
        }
    }

    @Volatile
    var weatherRemoteDataSource: WeatherRemoteDataSource? = null
        @VisibleForTesting set

    var baseUrl = "https://api.darksky.net/forecast/"

    fun provideWeatherRemoteDataSource(): WeatherRemoteDataSource {
        synchronized(lock) {
            return weatherRemoteDataSource ?: DefaultWeatherRemoteDataSource(
                baseUrl,
                OkHttpClient.Builder()
                    .apply {
                        if (BuildUtils.isDebug) {
                            addInterceptor(
                                HttpLoggingInterceptor().apply {
                                    level = HttpLoggingInterceptor.Level.BODY
                                }
                            )
                        }
                    }
                    .build()
            )
        }
    }
//
//    private fun provideWeatherRemoteDataSource() =
//        weatherRemoteDataSource ?: FakeWeatherRemoteDataSource()

    @Volatile
    var weatherLocalDataSource: WeatherLocalDataSource? = null
        @VisibleForTesting set

    private fun provideWeatherLocalDataSource(appContext: Context): WeatherLocalDataSource {
        synchronized(lock) {
            return weatherLocalDataSource ?: provideAppDataBase(appContext).weatherDao()
        }
    }

    @Volatile
    var database: AppDatabase? = null
        @VisibleForTesting set

    private fun provideAppDataBase(appContext: Context): AppDatabase {
        if (database == null) {
            synchronized(lock) {
                if (database === null) {
                    database = Room.databaseBuilder(
                        appContext, AppDatabase::class.java, "iiweather-db"
                    ).build()
                }
            }
        }
        return database!!
    }
}