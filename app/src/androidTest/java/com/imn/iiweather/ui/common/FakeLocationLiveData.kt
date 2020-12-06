package com.imn.iiweather.ui.common

import androidx.lifecycle.LiveData
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.locationModel

class FakeLocationLiveData : LiveData<LocationModel>() {
    override fun onActive() {
        super.onActive()
        value = locationModel
    }
}