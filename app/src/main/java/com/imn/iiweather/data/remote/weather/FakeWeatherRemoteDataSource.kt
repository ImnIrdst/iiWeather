package com.imn.iiweather.data.remote.weather

import com.imn.iiweather.data.repository.weather.WeatherRemoteDataSource
import com.imn.iiweather.domain.model.location.LocationModel

class FakeWeatherRemoteDataSource : WeatherRemoteDataSource {
    
    override suspend fun getCurrentWeather(locationModel: LocationModel) =
        WeatherResponse(
            json = """{
              "latitude": 37.3806,
              "longitude": -122.0593,
              "timezone": "America/Los_Angeles",
              "currently": {
                "time": 1607322814,
                "summary": "Clear",
                "icon": "clear-night",
                "nearestStormDistance": 15,
                "nearestStormBearing": 82,
                "precipIntensity": 0,
                "precipProbability": 0,
                "temperature": 49.05,
                "apparentTemperature": 47.94,
                "dewPoint": 39.13,
                "humidity": 0.69,
                "pressure": 1022.7,
                "windGust": 7.79,
                "windBearing": 319,
                "cloudCover": 0.13,
                "uvIndex": 0,
                "visibility": 10,
                "ozone": 286.6
              }
            }
            """
        )
}