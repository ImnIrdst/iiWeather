package com.imn.iiweather.data.repository.weather

import com.imn.iiweather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class DefaultWeatherRepository(
    private val local: WeatherLocalDataSource,
    private val remote: WeatherRemoteDataSource
) : WeatherRepository {

    override fun getCurrentWeather() = local.getCurrentWeather()
        .transform { localEntity ->
            emit(localEntity)
        }
        .filterNotNull()
        .map { it.toWeatherModel() }

}