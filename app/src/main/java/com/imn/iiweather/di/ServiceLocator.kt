package com.imn.iiweather.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.utils.State
import com.imn.iiweather.ui.common.location.LocationLiveData

object ServiceLocator {

    private val lock = Any()

    @Volatile
    var locationLiveData: LiveData<State<LocationModel>>? = null
        @VisibleForTesting set

    fun provideLocationLiveData(context: Context): LiveData<State<LocationModel>> {
        synchronized(lock) {
            return locationLiveData ?: LocationLiveData(context)
        }
    }
}