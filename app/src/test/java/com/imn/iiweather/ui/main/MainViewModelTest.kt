package com.imn.iiweather.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import com.google.common.truth.Truth.assertThat
import com.imn.iiweather.IITestCase
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.repository.LocationRepository
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.domain.utils.State
import com.imn.iiweather.locationModel
import com.imn.iiweather.utils.awaitValue
import com.imn.iiweather.weather
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest : IITestCase() {

    @get:Rule
    var instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val locationRepository: LocationRepository = mockk()
    private val weatherRepository: WeatherRepository = mockk()
    private val mainViewModel = MainViewModel(weatherRepository, locationRepository)

    @Before
    fun setUp() {

    }


    @Test
    fun test() = td.runBlockingTest {
        every { locationRepository.getLocationLiveData() } returns
                liveData<State<LocationModel>> { emit(State.Success(locationModel)) }
        every { weatherRepository.getCurrentWeather(locationModel) } returns
                flow { emit(weather) }

        mainViewModel.loadWeather().awaitValue().let {
            assertThat(it).isEqualTo(weather)
        }

        verifySequence {
            locationRepository.getLocationLiveData()
            weatherRepository.getCurrentWeather(locationModel)
        }
    }
}