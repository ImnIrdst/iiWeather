package com.imn.iiweather.domain.repository

import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.model.location.WeatherModel
import com.imn.iiweather.domain.utils.State
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeather(locationModel: LocationModel): Flow<State<WeatherModel>>
}
