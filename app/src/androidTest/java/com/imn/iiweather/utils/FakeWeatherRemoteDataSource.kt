package com.imn.iiweather.utils

import com.imn.iiweather.data.repository.weather.WeatherRemoteDataSource
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.weatherResponse

class FakeWeatherRemoteDataSource(
    private val exceptionToThrow: Throwable? = null,
) : WeatherRemoteDataSource {
    override suspend fun getCurrentWeather(locationModel: LocationModel) =
        if (exceptionToThrow == null) {
            weatherResponse
        } else {
            throw exceptionToThrow
        }
}