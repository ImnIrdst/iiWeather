package com.imn.iiweather.ui.main

import androidx.lifecycle.*
import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.model.location.WeatherModel
import com.imn.iiweather.domain.repository.LocationRepository
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.domain.utils.IIError
import com.imn.iiweather.domain.utils.Loading
import com.imn.iiweather.domain.utils.State

class MainViewModel(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository,
) : ViewModel() {
    fun loadWeather() = Transformations.switchMap(locationRepository.getLocationLiveData()) {
        when (it.value) {
            is LocationModel -> weatherRepository.getCurrentWeather(it.value)
                .asLiveData(viewModelScope.coroutineContext)
            is IIError -> liveData { emit(State<WeatherModel>(it.value)) }
            is Loading -> liveData { emit(State<WeatherModel>(it.value)) }
            else -> throw IllegalStateException()
        }
    }
}