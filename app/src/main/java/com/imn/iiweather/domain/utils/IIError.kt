package com.imn.iiweather.domain.utils

import com.imn.iiweather.R
import java.net.UnknownHostException

sealed class IIError(cause: Throwable) : Throwable(cause) {
    class Network(cause: Throwable) : IIError(cause)
    class Unknown(cause: Throwable) : IIError(cause)
    class Location(cause: Throwable) : IIError(cause)
}


fun Throwable.asIIError(): IIError =
    when (this) {
        is IIError -> this
        is UnknownHostException -> IIError.Network(this)
        else -> IIError.Unknown(this) // Also send it to crash reporting service
    }

fun IIError.humanReadable() = when (this) {
    is IIError.Location -> R.string.failed_to_get_location
    else -> R.string.unknown_error
}