package com.imn.iiweather.domain.model.location

import com.imn.iiweather.utils.toFormattedDate

data class WeatherModel(
    val time: Long?,
    val summary: String?,
    val temperature: Double?,
    val humidity: Double?,
    val pressure: Double?,
    val windSpeed: Double?,
    val longitude: Double?,
    val latitude: Double?,
) {
    val formattedDate = time?.toFormattedDate()
}