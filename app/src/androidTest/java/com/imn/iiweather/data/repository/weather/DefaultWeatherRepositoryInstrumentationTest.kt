package com.imn.iiweather.data.repository.weather

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.imn.iiweather.*
import com.imn.iiweather.data.local.AppDatabase
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.domain.model.location.WeatherModel
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.domain.utils.State
import com.imn.iiweather.utils.FakeDataBase
import com.imn.iiweather.utils.FakeWeatherRemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class DefaultWeatherRepositoryInstrumentationTest : IIAndroidTestCase() {
    private lateinit var repository: WeatherRepository
    private lateinit var fakeDatabase: AppDatabase

    @Before
    fun setUp() {
        val appContext: Context = ApplicationProvider.getApplicationContext()

        ServiceLocator.weatherRemoteDataSource = FakeWeatherRemoteDataSource()
        FakeDataBase.provideFakeDataBase(appContext).let {
            fakeDatabase = it
            ServiceLocator.database = it
        }
        repository = ServiceLocator.provideWeatherRepository(appContext)
    }

    @After
    fun tearDown() {
        FakeDataBase.clearAll()
    }

    @Test
    fun testWhenWeatherDoesNotExistInDatabase() = td.runBlockingTest {
        val result = mutableListOf<State<WeatherModel>>()
        val latch = CountDownLatch(2)
        val job = testScope.launch {
            repository.getCurrentWeather(locationModel)
                .collect {
                    result += it
                    latch.countDown()
                }
        }
        latch.await(LATCH_AWAIT_TIMEOUT, TimeUnit.SECONDS)
        job.cancel()
        assertThat(result).isEqualTo(
            listOf(
                State.loading(),
                State.success(weather),
            )
        )
    }

    @Test
    fun testWhenWeatherIsExpired() = td.runBlockingTest {
        val result = mutableListOf<State<WeatherModel>>()
        val latch = CountDownLatch(3)

        val job = testScope.launch {

            fakeDatabase.weatherDao()
                .insertCurrentWeather(expiredWeatherEntity)

            repository.getCurrentWeather(locationModel)
                .collect {
                    result += it
                    latch.countDown()
                }
        }
        latch.await(LATCH_AWAIT_TIMEOUT, TimeUnit.SECONDS)
        job.cancel()
        assertThat(result).isEqualTo(
            listOf(
                State.loading(),
                State.success(weather),
                State.success(weather)
            )
        )
    }
}