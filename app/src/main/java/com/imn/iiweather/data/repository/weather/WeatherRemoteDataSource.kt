package com.imn.iiweather.data.repository.weather

import com.imn.iiweather.data.remote.weather.WeatherResponse

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(): WeatherResponse
}