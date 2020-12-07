package com.imn.iiweather.data.repository.location

import androidx.lifecycle.LiveData
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.repository.LocationRepository
import com.imn.iiweather.domain.utils.State

class DefaultLocationRepository(
    private val fusedLocationLiveData: LiveData<State<LocationModel>>,
) : LocationRepository {
    override fun getLocationLiveData() = fusedLocationLiveData
}