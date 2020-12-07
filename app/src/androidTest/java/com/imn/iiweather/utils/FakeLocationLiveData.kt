package com.imn.iiweather.utils

import androidx.lifecycle.LiveData
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.utils.State
import com.imn.iiweather.locationError
import com.imn.iiweather.locationModel

class FakeLocationLiveData : LiveData<State<LocationModel>>() {
    override fun onActive() {
        super.onActive()
        value = State.loading()
    }

    fun loadLocation() {
        postValue(State.success(locationModel))
    }

    fun sendError() {
        postValue(State.failure(locationError))
    }
}