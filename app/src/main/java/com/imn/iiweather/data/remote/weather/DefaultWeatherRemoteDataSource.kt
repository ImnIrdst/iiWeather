package com.imn.iiweather.data.remote.weather

import com.imn.iiweather.BuildConfig
import com.imn.iiweather.data.repository.weather.WeatherRemoteDataSource
import com.imn.iiweather.domain.model.location.LocationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class DefaultWeatherRemoteDataSource(
    private val okHttpClient: OkHttpClient,
) : WeatherRemoteDataSource {

    override suspend fun getCurrentWeather(locationModel: LocationModel): WeatherResponse =
        withContext(Dispatchers.IO) {
            Request.Builder()
                .url(BASE_URL + BuildConfig.API_KEY + "/${locationModel.latitude},${locationModel.longitude}")
                .build()
                .let { okHttpClient.newCall(it).execute().body!! }
                .let { WeatherResponse(it.string()) }
        }

    companion object {
        const val BASE_URL = "https://api.darksky.net/forecast/"
    }
}