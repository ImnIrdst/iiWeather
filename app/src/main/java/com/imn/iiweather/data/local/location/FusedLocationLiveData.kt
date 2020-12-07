package com.imn.iiweather.data.local.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.utils.IIError
import com.imn.iiweather.domain.utils.State

class FusedLocationLiveData(context: Context) : LiveData<State<LocationModel>>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    @SuppressLint("MissingPermission")
    override fun onActive() {
        super.onActive()
        value = State.Loading
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                setLocationData(location)
            }
            .addOnFailureListener {
                sendError(null, it)
            }
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult == null) {
                sendError(locationResult)
                return
            }
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }

    private fun setLocationData(location: Location?) {
        if (location == null) {
            sendError(location)
        } else {
            value = State.Success(
                LocationModel(
                    longitude = location.longitude,
                    latitude = location.latitude
                )
            )
        }
    }

    private fun sendError(location: Location?, cause: Throwable? = null) {
        value = State.Failure(
            IIError.Location(Throwable("locationResult is $location", cause))
        )
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}