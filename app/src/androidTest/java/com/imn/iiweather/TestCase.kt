package com.imn.iiweather

import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.utils.IIError

val locationModel = LocationModel(
    longitude = -122.0593,
    latitude = 37.3806,
)

val locationError = IIError.Location(Throwable("test location error"))