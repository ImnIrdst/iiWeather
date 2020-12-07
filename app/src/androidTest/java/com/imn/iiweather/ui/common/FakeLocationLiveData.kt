package com.imn.iiweather.ui.common

import androidx.lifecycle.LiveData
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.utils.State
import com.imn.iiweather.locationError
import com.imn.iiweather.locationModel

class FakeLocationLiveData : LiveData<State<LocationModel>>() {
    override fun onActive() {
        super.onActive()
        value = State.Loading
    }

    fun loadLocation() {
        postValue(State.Success(locationModel))
    }

    fun sendError() {
        postValue(State.Failure(locationError))
    }
}