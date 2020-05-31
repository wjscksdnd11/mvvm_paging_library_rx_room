package com.jeon.pagingsample.data.dao

import androidx.paging.DataSource
import androidx.room.*
import com.jeon.pagingsample.data.entity.Hotel
import io.reactivex.Completable


@Dao
interface HotelDao {
    @Query("SELECT * FROM hotel")
    fun getDataSource(): DataSource.Factory<Int, Hotel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg hotels: Hotel): Completable

    @Query("Delete from hotel where id =:id")
    fun delete(id: Int): Completable
}