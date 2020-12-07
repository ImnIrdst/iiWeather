package com.imn.iiweather.data.repository.weather

import com.imn.iiweather.data.local.weather.WeatherEntity
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun getCurrentWeather(): Flow<WeatherEntity?>
    suspend fun insertCurrentWeather(weatherEntity: WeatherEntity)
}