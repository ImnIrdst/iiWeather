package com.imn.iiweather.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormattedDate(): String? =
    SimpleDateFormat("HH:mm:ss (EEE, d MMM yyyy)", Locale.ENGLISH).format(Date(this))
