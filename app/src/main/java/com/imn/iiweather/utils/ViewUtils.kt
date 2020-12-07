package com.imn.iiweather.utils

import android.widget.TextView
import androidx.core.view.isVisible

fun TextView.setTextOrGone(value: String?) {
    if (value == null) {
        isVisible = false
    } else {
        isVisible = true
        text = value
    }
}