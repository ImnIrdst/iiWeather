package com.imn.iiweather.domain.utils

sealed class State<out R> {
    object Loading : State<Nothing>()
    data class Success<out V>(val value: V) : State<V>()
    data class Failure(val error: IIError) : State<Nothing>()
}