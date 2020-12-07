package com.imn.iiweather.utils

import org.json.JSONException
import org.json.JSONObject

fun JSONObject.getStringOrNull(key: String) =
    try {
        getString(key)
    } catch (e: JSONException) {
        null
    }

fun JSONObject.getDoubleOrNull(key: String) =
    try {
        getDouble(key)
    } catch (e: JSONException) {
        null
    }

fun JSONObject.getLongOrNull(key: String) =
    try {
        getLong(key)
    } catch (e: JSONException) {
        null
    }

fun JSONObject.getJsonObjectOrNull(key: String) =
    try {
        getJSONObject(key)
    } catch (e: JSONException) {
        null
    }