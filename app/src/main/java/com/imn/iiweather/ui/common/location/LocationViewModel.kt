package com.imn.iiweather.ui.common.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.imn.iiweather.IIWeatherApp

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationLiveData = (application as IIWeatherApp).locationLiveData

    fun getLocationData() = locationLiveData
}