package com.imn.iiweather.data.local.weather

import androidx.room.*
import com.imn.iiweather.data.repository.weather.WeatherLocalDataSource
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao : WeatherLocalDataSource {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertCurrentWeather(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weather")
    override fun getCurrentWeather(): Flow<WeatherEntity?>
}
