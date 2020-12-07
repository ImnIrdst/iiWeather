package com.imn.iiweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imn.iiweather.data.local.weather.WeatherDao
import com.imn.iiweather.data.local.weather.WeatherEntity

@Database(
    entities = [
        WeatherEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}