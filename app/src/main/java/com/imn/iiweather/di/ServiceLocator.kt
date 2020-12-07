package com.imn.iiweather.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.imn.iiweather.data.local.FusedLocationLiveData
import com.imn.iiweather.data.repository.location.DefaultLocationRepository
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.repository.LocationRepository
import com.imn.iiweather.domain.utils.State

object ServiceLocator {

    private val lock = Any()

    @Volatile
    var fusedLocationLiveData: LiveData<State<LocationModel>>? = null
        @VisibleForTesting set

    @Volatile
    var locationRepository: LocationRepository? = null
        @VisibleForTesting set


    fun provideLocationRepository(applicationContext: Context): LocationRepository {
        synchronized(lock) {
            return locationRepository ?: DefaultLocationRepository(
                provideLocationLiveData(applicationContext)
            )
        }
    }


    private fun provideLocationLiveData(context: Context): LiveData<State<LocationModel>> {
        synchronized(lock) {
            return fusedLocationLiveData ?: FusedLocationLiveData(context)
        }
    }
}