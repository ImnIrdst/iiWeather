package com.imn.iiweather.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.imn.iiweather.data.local.FusedLocationLiveData
import com.imn.iiweather.data.local.weather.WeatherEntity
import com.imn.iiweather.data.remote.weather.WeatherResponse
import com.imn.iiweather.data.repository.location.DefaultLocationRepository
import com.imn.iiweather.data.repository.weather.DefaultWeatherRepository
import com.imn.iiweather.data.repository.weather.WeatherLocalDataSource
import com.imn.iiweather.data.repository.weather.WeatherRemoteDataSource
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.repository.LocationRepository
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.domain.utils.State
import com.imn.iiweather.ui.main.MainViewModelFactory
import kotlinx.coroutines.flow.Flow

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

    private fun provideWeatherRepository(applicationContext: Context): WeatherRepository {
        synchronized(lock) {
            return weatherRepository ?: DefaultWeatherRepository(
                provideWeatherLocalDataSource(applicationContext),
                provideWeatherRemoteDataSource(),
            )
        }
    }

    private fun provideWeatherRemoteDataSource(): WeatherRemoteDataSource {
        synchronized(lock) {
            return object : WeatherRemoteDataSource {
                override suspend fun getCurrentWeather(): WeatherResponse {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    private fun provideWeatherLocalDataSource(appContext: Context): WeatherLocalDataSource {
        synchronized(lock) {
            return object : WeatherLocalDataSource {
                override fun getCurrentWeather(): Flow<WeatherEntity?> {
                    TODO("Not yet implemented")
                }

                override suspend fun insertCurrentWeather(weatherEntity: WeatherEntity) {
                    TODO("Not yet implemented")
                }
            }
        }
    }
}