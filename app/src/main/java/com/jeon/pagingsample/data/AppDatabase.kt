package com.jeon.pagingsample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeon.pagingsample.data.dao.HotelDao
import com.jeon.pagingsample.data.entity.Hotel


@Database(entities = [Hotel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hotelDao(): HotelDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "hotel.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

    }
}