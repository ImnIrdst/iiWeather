package com.imn.iiweather.data.repository.weather

interface WeatherRemoteDataSource {
    suspend fun getWeather(): WeatherResponse
}