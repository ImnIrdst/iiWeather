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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeatherEntity

        if (id != other.id) return false
        if (time != other.time) return false
        if (summary != other.summary) return false
        if (temperature != other.temperature) return false
        if (humidity != other.humidity) return false
        if (pressure != other.pressure) return false
        if (windSpeed != other.windSpeed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (time?.hashCode() ?: 0)
        result = 31 * result + (summary?.hashCode() ?: 0)
        result = 31 * result + (temperature?.hashCode() ?: 0)
        result = 31 * result + (humidity?.hashCode() ?: 0)
        result = 31 * result + (pressure?.hashCode() ?: 0)
        result = 31 * result + (windSpeed?.hashCode() ?: 0)
        return result
    }


    companion object {
        const val EXPIRATION_THRESHOLD = 10_000
    }
}
