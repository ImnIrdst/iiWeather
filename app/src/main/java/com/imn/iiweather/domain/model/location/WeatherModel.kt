package com.imn.iiweather.domain.model.location

data class WeatherModel(
    val time: Long?,
    val summary: String?,
    val temperature: Double?,
    val humidity: Double?,
    val pressure: Double?,
    val windSpeed: Double?,
)