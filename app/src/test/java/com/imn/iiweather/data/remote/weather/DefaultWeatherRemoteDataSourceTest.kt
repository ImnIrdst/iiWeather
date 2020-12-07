package com.imn.iiweather.data.remote.weather

import com.google.common.truth.Truth.assertThat
import com.imn.iiweather.*
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.utils.BuildUtils
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalCoroutinesApi::class)
class DefaultWeatherRemoteDataSourceTest : IITestCase() {

    @Before
    fun setUp() {
        mockkObject(BuildUtils)
        every { BuildUtils.isDebug } returns false // Change this to see logs
    }

    @Test
    fun testNormalScenario() = td.runBlockingTest {
        val server = MockWebServer()

        val response = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody(weatherJson)

        server.enqueue(response)


        server.start()
        ServiceLocator.baseUrl = server.url("/forecast/").toString()
        val remote = ServiceLocator.provideWeatherRemoteDataSource()

        val latch = CountDownLatch(1)
        testScope.launch {
            remote.getCurrentWeather(locationModel)
                .let {
                    assertThat(it).isEqualTo(weatherResponse)
                    latch.countDown()
                }
        }


        latch.await(LATCH_AWAIT_TIMEOUT, TimeUnit.SECONDS)
        server.shutdown()
    }
}