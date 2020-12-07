package com.imn.iiweather.data.repository.weather

import com.imn.iiweather.domain.model.location.LocationModel
import com.imn.iiweather.domain.repository.WeatherRepository
import com.imn.iiweather.domain.utils.withStates
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class DefaultWeatherRepository(
    private val local: WeatherLocalDataSource,
    private val remote: WeatherRemoteDataSource
) : WeatherRepository {

    override fun getCurrentWeather(locationModel: LocationModel) = withStates {
        local.getCurrentWeather()
            .transform { localEntity ->
                emit(localEntity)

                if (localEntity == null || localEntity.isExpired()) {
                    remote.getCurrentWeather().toWeatherEntity()
                        ?.let { if (localEntity != null) it.copy(id = localEntity.id) else it }
                        ?.let { local.insertCurrentWeather(it) }
                }
            }
            .filterNotNull()
            .map { it.toWeatherModel() }
    }

}