package com.imn.iiweather.domain.model.location

import com.imn.iiweather.utils.getDoubleOrNull
import com.imn.iiweather.utils.getJsonObjectOrNull
import com.imn.iiweather.utils.getLongOrNull
import com.imn.iiweather.utils.getStringOrNull
import org.json.JSONObject

data class WeatherModel(
    val time: Long?,
    val summary: String?,
    val temperature: Double?,
    val humidity: Double?,
    val pressure: Double?,
    val windSpeed: Double?,
) {
    companion object {
        private const val CURRENTLY = "currently"
        private const val TIME = "time"
        private const val SUMMARY = "summary"
        private const val TEMPERATURE = "temperature"
        private const val HUMIDITY = "humidity"
        private const val PRESSURE = "pressure"
        private const val WIND_SPEED = "windSpeed"

        fun fromJson(json: String): WeatherModel? {
            JSONObject(json).let { root ->
                root.getJsonObjectOrNull(CURRENTLY)?.let { cur ->
                    return WeatherModel(
                        cur.getLongOrNull(TIME),
                        cur.getStringOrNull(SUMMARY),
                        cur.getDoubleOrNull(TEMPERATURE),
                        cur.getDoubleOrNull(HUMIDITY),
                        cur.getDoubleOrNull(PRESSURE),
                        cur.getDoubleOrNull(WIND_SPEED),
                    )
                }
            }
            return null
        }
    }
}