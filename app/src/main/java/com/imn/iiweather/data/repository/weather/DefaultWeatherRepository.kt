package com.imn.iiweather.data.repository.weather

import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.domain.utils.withStates
import com.imn.iiweather.utils.EspressoIdlingResource
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class DefaultWeatherRepository(
    private val local: WeatherLocalDataSource,
    private val remote: WeatherRemoteDataSource
) : WeatherRepository {

    override fun getCurrentWeather(locationModel: LocationModel) = withStates {
        EspressoIdlingResource.increment()
        local.getCurrentWeather()
            .transform { localEntity ->
                EspressoIdlingResource.decrement()
                emit(localEntity)

                if (localEntity == null || localEntity.isExpired()) {
                    EspressoIdlingResource.increment()
                    remote.getCurrentWeather(locationModel).toWeatherEntity()
                        ?.let { if (localEntity != null) it.copy(id = localEntity.id) else it }
                        ?.let { local.insertCurrentWeather(it) }
                }
            }
            .filterNotNull()
            .map { it.toWeatherModel() }
    }
}