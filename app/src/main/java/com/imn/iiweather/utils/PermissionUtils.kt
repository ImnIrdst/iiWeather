package com.imn.iiweather.utils

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.checkSelfPermissionCompat(permissions: Array<String>) =
    requireContext().isGranted(permissions)


fun Fragment.shouldShowRequestPermissionRationaleCompat(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
            return true
        }
    }
    return false
}

fun Context.isGranted(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}