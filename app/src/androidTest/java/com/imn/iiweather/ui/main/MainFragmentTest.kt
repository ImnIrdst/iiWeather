package com.imn.iiweather.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.imn.iiweather.*
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.domain.utils.humanReadable
import com.imn.iiweather.utils.EspressoIdlingResource
import com.imn.iiweather.utils.FakeDataBase
import com.imn.iiweather.utils.FakeLocationLiveData
import com.imn.iiweather.utils.FakeWeatherRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    private lateinit var fakeLocationLiveData: FakeLocationLiveData

    val context: Context = ApplicationProvider.getApplicationContext()
    val fakeDataBase = FakeDataBase.provideFakeDataBase(context)

    @Before
    fun before() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        fakeLocationLiveData = FakeLocationLiveData()
        ServiceLocator.fusedLocationLiveData = fakeLocationLiveData
        ServiceLocator.database = fakeDataBase
        ServiceLocator.weatherRemoteDataSource = FakeWeatherRemoteDataSource()
    }

    fun after() {
        fakeDataBase.clearAllTables()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun showUserLocation_success() {

        launchFragmentInContainer<MainFragment>(Bundle(), R.style.Theme_IIWeather_NoActionBar)

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.flow_container)).check(matches(not(isDisplayed())))

        fakeLocationLiveData.loadLocation()

        checkIfWeatherDataIsShown()
    }

    private fun checkIfWeatherDataIsShown() {
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))

        onView(withId(R.id.flow_container)).check(matches(isDisplayed()))
        onView(withId(R.id.time_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.summary_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.temperature_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.humidity_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.pressure_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.windSpeed_text_view)).check(matches(not(isDisplayed())))
        onView(withId(R.id.latitude_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.longitude_text_view)).check(matches(isDisplayed()))

        onView(withId(R.id.time_text_view))
            .check(matches(withText(context.getString(R.string.time_, weather.timeFormatted))))

        onView(withId(R.id.summary_text_view))
            .check(matches(withText(context.getString(R.string.summary_, weather.summary))))

        onView(withId(R.id.temperature_text_view))
            .check(matches(withText(context.getString(R.string.temperature_, weather.temperature))))

        onView(withId(R.id.humidity_text_view))
            .check(matches(withText(context.getString(R.string.humidity_, weather.humidity))))

        onView(withId(R.id.pressure_text_view))
            .check(matches(withText(context.getString(R.string.pressure_, weather.pressure))))

        onView(withId(R.id.longitude_text_view))
            .check(matches(withText(context.getString(R.string.longitude_, weather.longitude))))

        onView(withId(R.id.latitude_text_view))
            .check(matches(withText(context.getString(R.string.latitude_, weather.latitude))))
    }

    @Test
    fun showUserLocation_error() {

        launchFragmentInContainer<MainFragment>(Bundle(), R.style.Theme_IIWeather_NoActionBar)

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))

        fakeLocationLiveData.sendError()

        onView(withId(R.id.flow_container)).check(matches(not(isDisplayed())))

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(locationError.humanReadable())))
    }

    @Test
    fun onlyLoadsOfflineData() {
        runBlocking {
            fakeDataBase.weatherDao().insertCurrentWeather(expiredWeatherEntity)
        }
        ServiceLocator.weatherRemoteDataSource = FakeWeatherRemoteDataSource(unknownHostException)

        launchFragmentInContainer<MainFragment>(Bundle(), R.style.Theme_IIWeather_NoActionBar)

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.flow_container)).check(matches(not(isDisplayed())))

        fakeLocationLiveData.loadLocation()

        checkIfWeatherDataIsShown()

        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(iiExpiredError.humanReadable())))
    }
}