package com.imn.iiweather.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.ui.common.location.LocationLiveData

object ServiceLocator {

    private val lock = Any()

    @Volatile
    var locationLiveData: LiveData<LocationModel>? = null
        @VisibleForTesting set

    fun provideLocationLiveData(context: Context): LiveData<LocationModel> {
        synchronized(lock) {
            return locationLiveData ?: LocationLiveData(context)
        }
    }
}