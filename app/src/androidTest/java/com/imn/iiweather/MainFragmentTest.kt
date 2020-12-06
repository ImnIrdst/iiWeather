package com.imn.iiweather

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class MainFragmentTest {

    @Test
    fun userLocationIsShown() {
        launchFragmentInContainer<MainFragment>(Bundle(), R.style.Theme_IIWeather_NoActionBar)

        onView(withId(R.id.lat_text_view)).check(matches(isDisplayed()))
        onView(withId(R.id.long_text_view)).check(matches(isDisplayed()))
        
        onView(withId(R.id.lat_text_view)).check(matches(withText("37.3806")))
        onView(withId(R.id.long_text_view)).check(matches(withText("-122.0593")))

        Thread.sleep(2000)
    }
}