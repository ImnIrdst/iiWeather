package com.imn.iiweather

import android.app.Application
import com.imn.iiweather.di.ServiceLocator
import com.imn.iiweather.ui.main.MainViewModelFactory

class IIWeatherApp : Application() {

    val mainViewModelFactory: MainViewModelFactory
        get() = ServiceLocator.provideMainViewModelFactory(this)
}