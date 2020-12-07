package com.imn.iiweather.domain.model.location

import com.google.common.truth.Truth.assertThat
import com.imn.iiweather.weather
import com.imn.iiweather.weatherJson
import org.junit.Test

class WeatherTest {

    @Test
    fun testFromJson() {
        val actual = Weather.fromJson(weatherJson)

        assertThat(actual).isEqualTo(weather)
    }
}