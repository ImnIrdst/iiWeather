package com.imn.iiweather.data.repository.weather

import com.google.common.truth.Truth.assertThat
import com.imn.iiweather.IITestCase
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.weather
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultWeatherRepositoryTest : IITestCase() {

    private val local = mockk<WeatherLocalDataSource>()
    private val remote = mockk<WeatherRemoteDataSource>()
    private val repository: WeatherRepository = DefaultWeatherRepository(local, remote)

    @After
    fun tearDown() {
        confirmVerified(local, remote)
    }

    @Test
    fun `test when current weather exists in db`() = td.runBlockingTest {
        repository.getCurrentWeather()
            .toList()
            .also {
                assertThat(it).isEqualTo(listOf(weather))
            }

        coVerify {
            local.getCurrentWeather(System.currentTimeMillis())
        }
    }
}