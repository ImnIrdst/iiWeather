package com.imn.iiweather.utils

import android.content.Context
import androidx.room.Room
import com.imn.iiweather.data.local.AppDatabase

object FakeDataBase {

    private val lock = Any()

    @Volatile
    private var database: AppDatabase? = null

    fun provideFakeDataBase(appContext: Context): AppDatabase {
        if (database == null) {
            synchronized(lock) {
                if (database === null) {
                    database = Room.inMemoryDatabaseBuilder(
                        appContext, AppDatabase::class.java
                    ).allowMainThreadQueries().build()
                }
            }
        }
        return database!!
    }

    fun clearAll() {
        database?.clearAllTables()
    }
}