package com.imn.iiweather.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.imn.iiweather.domain.model.location.WeatherModel
import com.imn.iiweather.domain.repository.LocationRepository
import com.imn.iiweather.domain.repository.WeatherRepository

class MainViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {
    fun getLocationData() = locationRepository.getLocationLiveData()

    fun loadWeather(): LiveData<WeatherModel> {
        TODO("Not yet implemented")
    }
}