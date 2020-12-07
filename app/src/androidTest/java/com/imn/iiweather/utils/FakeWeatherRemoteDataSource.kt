package com.imn.iiweather.utils

import com.imn.iiweather.data.repository.weather.WeatherRemoteDataSource
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.weatherResponse

class FakeWeatherRemoteDataSource : WeatherRemoteDataSource {
    override suspend fun getCurrentWeather(locationModel: LocationModel) = weatherResponse
}