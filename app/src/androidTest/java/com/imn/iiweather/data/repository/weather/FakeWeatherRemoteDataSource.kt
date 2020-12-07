package com.imn.iiweather.data.repository.weather

import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.weatherResponse

class FakeWeatherRemoteDataSource : WeatherRemoteDataSource {
    override suspend fun getCurrentWeather(locationModel: LocationModel) = weatherResponse
}