package com.imn.iiweather

import android.app.Application
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.domain.repository.LocationRepository

class IIWeatherApp : Application() {

    val locationRepository: LocationRepository
        get() = ServiceLocator.provideLocationRepository(this)
}