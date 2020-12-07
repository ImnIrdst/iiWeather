package com.imn.iiweather

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
open class IIAndroidTestCase {

    protected val td = TestCoroutineDispatcher()
    protected val testScope = TestCoroutineScope(td)

    @Before
    fun iiSetUp() {
        Dispatchers.setMain(td)
    }

    @After
    fun iiTearDown() {
        testScope.uncaughtExceptions.firstOrNull()?.let { throw it }
        unmockkAll()
        Dispatchers.resetMain()
    }
}