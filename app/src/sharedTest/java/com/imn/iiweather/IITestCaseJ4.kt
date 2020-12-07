package com.imn.iiweather

import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Ignore

@OptIn(ExperimentalCoroutinesApi::class)
@Ignore("This is base class")
open class IITestCase {

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