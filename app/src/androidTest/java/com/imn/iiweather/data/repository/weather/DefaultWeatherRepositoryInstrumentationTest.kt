package com.imn.iiweather.data.repository.weather

import androidx.test.platform.app.InstrumentationRegistry
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DefaultWeatherRepositoryInstrumentationTest {

    private lateinit var repository: WeatherRepository

    @Before
    fun setUp() {
        ServiceLocator.weatherRemoteDataSource = FakeWeatherRemoteDataSource()

        repository = ServiceLocator.provideWeatherRepository(
            InstrumentationRegistry.getInstrumentation().context
        )
    }

    @Test
    fun test() = runBlocking {
        repository.getCurrentWeather()
            .collect {
                println(it)
            }
    }
}