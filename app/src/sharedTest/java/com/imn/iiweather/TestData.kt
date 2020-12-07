package com.imn.iiweather

import com.imn.iiweather.data.local.weather.WeatherEntity
import com.imn.iiweather.data.remote.weather.WeatherResponse
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.model.location.WeatherModel
import com.imn.iiweather.domain.utils.IIError
import java.net.UnknownHostException

val locationModel = LocationModel(
    longitude = -122.0593,
    latitude = 37.3806,
)

val locationError = IIError.Location(Throwable("test location error"))

val weather = WeatherModel(
    time = 1607322814,
    summary = "Clear",
    temperature = 49.05,
    humidity = 0.69,
    pressure = 1022.7,
    windSpeed = null,
    longitude = -122.0593,
    latitude = 37.3806,
)

val weatherEntity = WeatherEntity(
    time = 1607322814,
    summary = "Clear",
    temperature = 49.05,
    humidity = 0.69,
    pressure = 1022.7,
    windSpeed = null,
    longitude = -122.0593,
    latitude = 37.3806,
    creationTime = System.currentTimeMillis()
)

val expiredWeatherEntity = weatherEntity
    .copy(creationTime = System.currentTimeMillis() - 100_000)

val weatherResponse = WeatherResponse(weatherJson)

val unknownHostException = UnknownHostException()
val iiNetworkError = IIError.Network(unknownHostException)