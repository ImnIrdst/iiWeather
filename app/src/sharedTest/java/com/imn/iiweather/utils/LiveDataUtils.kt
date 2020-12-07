package com.imn.iiweather.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.imn.iiweather.LATCH_AWAIT_TIMEOUT
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> LiveData<T>.awaitValue(): T {
    val data = arrayOfNulls<Any>(1)
    val latch = CountDownLatch(1)

    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data[0] = o
            latch.countDown()
            removeObserver(this)
        }
    }
    observeForever(observer)
    latch.await(LATCH_AWAIT_TIMEOUT, TimeUnit.SECONDS)

    @Suppress("UNCHECKED_CAST")
    return data[0] as T
}