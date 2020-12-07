package com.imn.iiweather.data.repository.weather

import com.imn.iiweather.data.remote.weather.WeatherResponse
import com.imn.iiweather.domain.model.location.LocationModel

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(locationModel: LocationModel): WeatherResponse
}