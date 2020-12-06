package com.imn.iiweather.ui.main

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.imn.iiweather.R
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.domain.model.location.LocationModel
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    private val locationLiveData = MutableLiveData<LocationModel>()

    @Test
    fun userLocationIsShown() {
        ServiceLocator.locationLiveData = locationLiveData
        locationLiveData.postValue(LocationModel(-122.0593, 37.3806))

        launchFragmentInContainer<MainFragment>(Bundle(), R.style.Theme_IIWeather_NoActionBar)

        onView(withId(R.id.latitude_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.longitude_text_view)).check(matches(isDisplayed()))

        onView(withId(R.id.latitude_text_view)).check(matches(withText("Latitude: 37.3806")))
        onView(withId(R.id.longitude_text_view)).check(matches(withText("Longitude: -122.0593")))

        Thread.sleep(3000)
    }
}