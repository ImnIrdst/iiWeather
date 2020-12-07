package com.imn.iiweather.domain.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

data class State<out T>(
    val value: Any?,
) {
    val isSuccess: Boolean get() = value !is IIError && value !is Loading
    val isLoading: Boolean get() = value is Loading
    val isFailure: Boolean get() = value is IIError

    companion object {
        fun <T> success(value: T): State<T> =
            State(value)

        fun <T> failure(exception: Throwable): State<T> =
            State(exception.asIIError())

        fun <T> failure(iiError: IIError): State<T> =
            State(iiError)

        fun <T> loading(): State<T> =
            State(Loading)
    }
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
fun <T> withStates(debounce: Long = 0, flowProvider: () -> Flow<T>): Flow<State<T>> {
    return flow {
        emit(State.loading())

        flowProvider.invoke()
            .debounce(debounce)
            .mapLatest { State.success(it) }
            .catch { emit(State.failure(it.asIIError())) }
            .also { emitAll(it) }
    }
}