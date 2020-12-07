package com.imn.iiweather.domain.repository

import androidx.lifecycle.LiveData
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.utils.State

interface LocationRepository {
    fun getLocationLiveData(): LiveData<State<LocationModel>>
}
