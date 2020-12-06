package com.imn.iiweather

import android.app.Application
import androidx.lifecycle.LiveData
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.domain.model.location.LocationModel

class IIWeatherApp : Application() {

    val locationLiveData: LiveData<LocationModel>
        get() = ServiceLocator.provideLocationLiveData(this)
}