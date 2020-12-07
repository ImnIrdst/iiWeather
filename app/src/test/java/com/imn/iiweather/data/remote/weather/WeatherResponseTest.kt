package com.imn.iiweather.data.remote.weather

import com.google.common.truth.Truth.assertThat
import com.imn.iiweather.weatherEntity
import com.imn.iiweather.weatherResponse
import org.junit.Test

class WeatherResponseTest {

    @Test
    fun testJsonParsing() {
        val actual = weatherResponse.toWeatherEntity()

        assertThat(actual).isEqualTo(weatherEntity)
    }
}