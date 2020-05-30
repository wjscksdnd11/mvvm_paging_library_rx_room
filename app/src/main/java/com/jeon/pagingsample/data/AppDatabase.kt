package com.jeon.pagingsample.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jeon.pagingsample.dao.HotelDao

@Database(entities = [Hotel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): HotelDao
}