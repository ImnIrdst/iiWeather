package com.imn.iiweather.data.remote.weather

import com.imn.iiweather.data.local.weather.WeatherEntity
import com.imn.iiweather.utils.getDoubleOrNull
import com.imn.iiweather.utils.getJsonObjectOrNull
import com.imn.iiweather.utils.getLongOrNull
import com.imn.iiweather.utils.getStringOrNull
import org.json.JSONObject

class WeatherResponse(
    private val json: String,
) {

    fun toWeatherEntity(): WeatherEntity? {
        JSONObject(json).let { root ->
            root.getJsonObjectOrNull(CURRENTLY)?.let { cur ->
                return WeatherEntity(
                    time = cur.getLongOrNull(TIME),
                    summary = cur.getStringOrNull(SUMMARY),
                    temperature = cur.getDoubleOrNull(TEMPERATURE),
                    humidity = cur.getDoubleOrNull(HUMIDITY),
                    pressure = cur.getDoubleOrNull(PRESSURE),
                    windSpeed = cur.getDoubleOrNull(WIND_SPEED),
                    latitude = root.getDoubleOrNull(LATITUDE),
                    longitude = root.getDoubleOrNull(LONGITUDE),
                    creationTime = System.currentTimeMillis()
                )
            }
        }
        return null
    }

    companion object {
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
        private const val CURRENTLY = "currently"
        private const val TIME = "time"
        private const val SUMMARY = "summary"
        private const val TEMPERATURE = "temperature"
        private const val HUMIDITY = "humidity"
        private const val PRESSURE = "pressure"
        private const val WIND_SPEED = "windSpeed"
    }
}
