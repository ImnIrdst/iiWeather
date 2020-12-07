package com.imn.iiweather.data.local.weather

import com.imn.iiweather.domain.model.location.WeatherModel

data class WeatherEntity(
    var id: Long? = null,
    val time: Long?,
    val summary: String?,
    val temperature: Double?,
    val humidity: Double?,
    val pressure: Double?,
    val windSpeed: Double?,
    val creationTime: Long,
) {
    fun isExpired() = System.currentTimeMillis() - creationTime > EXPIRATION_THRESHOLD

    fun toWeatherModel() = WeatherModel(
        time = time,
        summary = summary,
        temperature = temperature,
        humidity = humidity,
        pressure = pressure,
        windSpeed = windSpeed,
    )

    companion object {
        const val EXPIRATION_THRESHOLD = 10_000
    }
}
