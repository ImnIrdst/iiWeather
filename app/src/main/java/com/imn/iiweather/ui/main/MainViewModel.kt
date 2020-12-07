package com.imn.iiweather.ui.main

import androidx.lifecycle.ViewModel
import com.imn.iiweather.domain.repository.LocationRepository
import com.imn.iiweather.domain.repository.WeatherRepository

class MainViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {
    fun getLocationData() = locationRepository.getLocationLiveData()
}