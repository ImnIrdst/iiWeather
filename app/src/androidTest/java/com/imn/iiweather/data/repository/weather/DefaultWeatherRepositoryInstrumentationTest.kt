package com.imn.iiweather.data.repository.weather

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.imn.iiweather.IITestCase
import com.imn.iiweather.data.local.AppDatabase
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.domain.model.location.WeatherModel
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.expiredWeatherEntity
import com.imn.iiweather.weather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultWeatherRepositoryInstrumentationTest : IITestCase() {

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
        val result = mutableListOf<WeatherModel>()
        val latch = CountDownLatch(1)
        val job = testScope.launch {
            repository.getCurrentWeather(locationModel)
                .collect {
                    result += it
                    latch.countDown()
                }
        }
        latch.await()
        job.cancel()
        assert(result == listOf(weather))
    }

    @Test
    fun testWhenWeatherIsExpired() = td.runBlockingTest {
        val result = mutableListOf<WeatherModel>()
        val latch = CountDownLatch(2)

        val job = testScope.launch {

            fakeDatabase.weatherDao()
                .insertCurrentWeather(expiredWeatherEntity)

            repository.getCurrentWeather(locationModel)
                .collect {
                    result += it
                    latch.countDown()
                }
        }
        latch.await(5, TimeUnit.SECONDS)
        job.cancel()
        assert(result == listOf(weather, weather))
    }
}