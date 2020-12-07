package com.imn.iiweather.ui.main

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.imn.iiweather.R
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.domain.utils.humanReadable
import com.imn.iiweather.locationError
import com.imn.iiweather.locationModel
import com.imn.iiweather.ui.common.FakeLocationLiveData
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    private lateinit var fakeLocationLiveData: FakeLocationLiveData

    @Before
    fun before() {
        fakeLocationLiveData = FakeLocationLiveData()
        ServiceLocator.fusedLocationLiveData = fakeLocationLiveData
    }

    @Test
    fun showUserLocation_success() {

        launchFragmentInContainer<MainFragment>(Bundle(), R.style.Theme_IIWeather_NoActionBar)

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.latitude_text_view)).check(matches(withText("")))
        onView(withId(R.id.longitude_text_view)).check(matches(withText("")))

        fakeLocationLiveData.loadLocation()

        onView(withId(R.id.latitude_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.longitude_text_view)).check(matches(isDisplayed()))

        onView(withId(R.id.latitude_text_view)).check(matches(withText("Latitude: ${locationModel.latitude}")))
        onView(withId(R.id.longitude_text_view)).check(matches(withText("Longitude: ${locationModel.longitude}")))
    }

    @Test
    fun showUserLocation_error() {

        launchFragmentInContainer<MainFragment>(Bundle(), R.style.Theme_IIWeather_NoActionBar)

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.latitude_text_view)).check(matches(withText("")))
        onView(withId(R.id.longitude_text_view)).check(matches(withText("")))

        fakeLocationLiveData.sendError()

        onView(withId(R.id.latitude_text_view)).check(matches(withText("")))
        onView(withId(R.id.longitude_text_view)).check(matches(withText("")))
        onView(withId(com.google.android.material.R.id.snackbar_text))
            .check(matches(withText(locationError.humanReadable())))
    }
}