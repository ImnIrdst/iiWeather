package com.imn.iiweather.data.repository.weather

import com.google.common.truth.Truth.assertThat
import com.imn.iiweather.*
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.domain.utils.IIError
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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

        repository.getCurrentWeather(locationModel)
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

        val localResultStateFlow = MutableStateFlow(expiredWeatherEntity)
        every { local.getCurrentWeather() } returns localResultStateFlow

        coEvery { remote.getCurrentWeather(locationModel) } answers {
            localResultStateFlow.value = weatherEntity
            weatherResponse
        }

        testScope.launch {
            repository.getCurrentWeather(locationModel)
                .toList()
                .also {
                    assertThat(it).isEqualTo(listOf(weather))
                }
        }
        coVerify {
            local.getCurrentWeather()
            remote.getCurrentWeather(locationModel)
            local.insertCurrentWeather(weatherEntity)
        }
    }

    @Test
    fun `test when remote throws exception`() = td.runBlockingTest {
        every { local.getCurrentWeather() } returns listOf(expiredWeatherEntity).asFlow()
        coEvery { remote.getCurrentWeather(locationModel) } throws unknownHostException

        testScope.launch {
            repository.getCurrentWeather(locationModel)
                .catch { assertThat(it).isInstanceOf(IIError.Network::class.java) }
                .collect()
        }

        coVerify {
            local.getCurrentWeather()
            remote.getCurrentWeather(locationModel)
        }
    }
}