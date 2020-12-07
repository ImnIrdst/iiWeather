package com.imn.iiweather.domain.repository

import com.imn.iiweather.domain.model.location.WeatherModel
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeather(): Flow<WeatherModel>
}
