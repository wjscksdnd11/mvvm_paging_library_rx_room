package com.jeon.pagingsample.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeon.pagingsample.data.AppDatabase
import com.jeon.pagingsample.data.HotelItem
import com.jeon.pagingsample.data.api.HotelService
import com.jeon.pagingsample.data.dto.toHotelItem
import com.jeon.pagingsample.data.entity.Hotel
import com.jeon.pagingsample.data.entity.toHotelItem
import com.jeon.pagingsample.data.source.HotelsDataSourceFactory
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class HotelRepository private constructor(
    application: Application,
    private val compositeDisposable: CompositeDisposable
) {
    companion object {
        @Volatile
        private var INSTANCE: HotelRepository? = null

        fun getInstance(application: Application,compositeDisposable: CompositeDisposable): HotelRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: HotelRepository(application,compositeDisposable)
                    .also { INSTANCE = it }
            }
        }
    }
    private val sourceDao = AppDatabase.getInstance(application)?.hotelDao()
    val sourceFactory: HotelsDataSourceFactory =
        HotelsDataSourceFactory(compositeDisposable, HotelService.getService())
    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20*2)
        .setEnablePlaceholders(false)
        .build()

    fun getPagedHotelList(): LiveData<PagedList<HotelItem>> {
        return LivePagedListBuilder(
            sourceFactory.map {item->
                item.toHotelItem()
            },
            config
        ).build()
    }

    fun retry() {
        sourceFactory.hotelsDataSourceLive.value?.retry()
    }

    fun getLiekedHotelList(sort: SORT, value: FIELD): LiveData<PagedList<HotelItem>> {
        return sourceDao?.getDataSource()
            ?.map { it.toHotelItem().also { item->item.isLike=true } }
            ?.mapByPage {
                if (sort == SORT.ASC) {
                    if(value==FIELD.RATE)
                        it.sortedBy { item->item.rate }
                    else
                        it.sortedBy { item->item.timemillis }
                } else {
                    if(value==FIELD.RATE)
                        it.sortedByDescending { item->item.rate }
                    else
                        it.sortedByDescending { item->item.timemillis }
                }
            }
            ?.let { LivePagedListBuilder(it, config).build() }?:MutableLiveData()
    }

    fun remove(id:Int) {
        sourceDao?.delete(id)
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {}
            ?.let { compositeDisposable.add(it) }
    }

    fun insert(hotelDao: Hotel) {
        sourceDao?.insertAll(hotelDao)
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {}
            ?.let { compositeDisposable.add(it) }
    }

    enum class SORT {
        ASC, DESC
    }

    enum class FIELD {
        RATE, DATE

    }

}