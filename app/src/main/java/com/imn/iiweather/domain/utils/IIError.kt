package com.imn.iiweather.domain.utils

import android.util.Log
import com.imn.iiweather.R
import com.imn.iiweather.utils.BuildUtils
import java.net.UnknownHostException

sealed class IIError(cause: Throwable) : Throwable(cause) {
    class Network(cause: Throwable) : IIError(cause)
    class ExpiredData(cause: Throwable) : IIError(cause)
    class Location(cause: Throwable) : IIError(cause)
    class Unknown(cause: Throwable) : IIError(cause) {
        init {
            if (BuildUtils.isDebug) {
                Log.e("IIError",
                    "unknown error",
                    cause) // also send this to crash reporting service
                throw cause
            }
        }
    }

    override fun equals(other: Any?): Boolean = other is IIError && cause == other.cause
    override fun hashCode(): Int = cause.hashCode()
    override fun toString(): String = "IIError($cause)"
}


fun Throwable.asIIError(): IIError =
    when (this) {
        is IIError -> this
        is UnknownHostException -> IIError.Network(this)
        else -> IIError.Unknown(this)
    }.also {
        if (BuildUtils.isDebug) {
            Log.e("IIError", "Throwable.asIIError $it", it)
        }
    }

fun IIError.humanReadable() = when (this) {
    is IIError.Location -> R.string.failed_to_get_location
    is IIError.Network -> R.string.failed_to_get_weather
    is IIError.ExpiredData -> R.string.failed_to_get_weather_expired_data
    else -> R.string.unknown_error
}