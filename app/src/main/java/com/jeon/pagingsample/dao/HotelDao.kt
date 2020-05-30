package com.jeon.pagingsample.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jeon.pagingsample.data.Hotel

@Dao
interface HotelDao {
    @Query("SELECT * FROM hotel")
    fun getAll(): List<Hotel>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Hotel>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Hotel

    @Insert
    fun insertAll(vararg users: Hotel)

    @Delete
    fun delete(user: Hotel)
}