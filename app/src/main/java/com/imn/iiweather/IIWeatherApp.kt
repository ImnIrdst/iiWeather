package com.imn.iiweather

import android.app.Application
import androidx.lifecycle.LiveData
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.utils.State

class IIWeatherApp : Application() {

    val locationLiveData: LiveData<State<LocationModel>>
        get() = ServiceLocator.provideLocationLiveData(this)
}