package com.imn.iiweather.data.repository.weather

import com.imn.iiweather.domain.model.location.Weather
import com.imn.iiweather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow

class DefaultWeatherRepository(
    private val local: WeatherLocalDataSource,
    private val remote: WeatherRemoteDataSource
) : WeatherRepository {

    override fun getCurrentWeather(): Flow<Weather> {
        TODO("Not yet implemented")
    }

}