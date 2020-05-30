package com.jeon.pagingsample.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jeon.pagingsample.dao.HotelDao
import com.jeon.pagingsample.data.entity.Hotel

@Database(entities = [Hotel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): HotelDao
}