package com.imn.iiweather.domain.model.location

import android.content.Context
import com.imn.iiweather.R
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
    fun getTimeFormatted(context: Context) =
        time?.let { context.getString(R.string.time_, it.toFormattedDate()) }

    fun getSummaryFormatted(context: Context) =
        summary?.let { context.getString(R.string.summary_, it) }

    fun getTemperatureFormatted(context: Context) =
        temperature?.let { context.getString(R.string.temperature_, it) }

    fun getHumidityFormatted(context: Context) =
        humidity?.let { context.getString(R.string.humidity_, it) }

    fun getPressureFormatted(context: Context) =
        pressure?.let { context.getString(R.string.pressure_, it) }

    fun getWindSpeedFormatted(context: Context) =
        windSpeed?.let { context.getString(R.string.windSpeed_, it) }

    fun getLongitudeFormatted(context: Context) =
        longitude?.let { context.getString(R.string.longitude_, it) }

    fun getLatitudeFormatted(context: Context) =
        latitude?.let { context.getString(R.string.latitude_, it) }
}