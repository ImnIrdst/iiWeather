package com.imn.iiweather.ui.common.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.imn.iiweather.IIWeatherApp

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationRepository = (application as IIWeatherApp).locationRepository

    fun getLocationData() = locationRepository.getLocationLiveData()
}