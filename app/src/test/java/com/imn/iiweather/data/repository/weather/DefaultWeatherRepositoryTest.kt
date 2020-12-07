package com.imn.iiweather.data.repository.weather

import com.google.common.truth.Truth.assertThat
import com.imn.iiweather.IITestCase
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.weather
import com.imn.iiweather.weatherEntity
import com.imn.iiweather.weatherResponse
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultWeatherRepositoryTest : IITestCase() {

    private val local = mockk<WeatherLocalDataSource>(relaxUnitFun = true)
    private val remote = mockk<WeatherRemoteDataSource>(relaxUnitFun = true)
    private val repository: WeatherRepository = DefaultWeatherRepository(local, remote)

    @After
    fun tearDown() {
        confirmVerified(local, remote)
    }

    @Test
    fun `test when current weather exists in db`() = td.runBlockingTest {
        every { local.getCurrentWeather() } returns listOf(weatherEntity).asFlow()

        repository.getCurrentWeather()
            .toList()
            .also {
                assertThat(it).isEqualTo(listOf(weather))
            }

        coVerify {
            local.getCurrentWeather()
        }
    }


    @Test
    fun `test when current weather is expired`() = td.runBlockingTest {
        val expiredWeatherEntity = weatherEntity
            .copy(creationTime = System.currentTimeMillis() - 100_000)

        val localResultStateFlow = MutableStateFlow(expiredWeatherEntity)
        every { local.getCurrentWeather() } returns localResultStateFlow

        coEvery { remote.getCurrentWeather() } answers {
            localResultStateFlow.value = weatherEntity
            weatherResponse
        }

        testScope.launch {
            repository.getCurrentWeather()
                .toList()
                .also {
                    assertThat(it).isEqualTo(listOf(weather))
                }
        }

        coVerify {
            local.getCurrentWeather()
            remote.getCurrentWeather()
            local.insertCurrentWeather(weatherEntity)
        }
    }
}