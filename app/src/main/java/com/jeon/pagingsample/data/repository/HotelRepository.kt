package com.jeon.pagingsample.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeon.pagingsample.data.AppDatabase
import com.jeon.pagingsample.data.HotelItem
import com.jeon.pagingsample.data.api.HotelService
import com.jeon.pagingsample.data.dto.toHotelItem
import com.jeon.pagingsample.data.entity.toHotelItem
import com.jeon.pagingsample.data.source.HotelsDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class HotelRepository(
    application: Application,
    compositeDisposable: CompositeDisposable
) {

    private val sourceDao = AppDatabase.getInstance(application)?.hotelDao()
    val sourceFactory: HotelsDataSourceFactory =
        HotelsDataSourceFactory(compositeDisposable, HotelService.getService())
    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setInitialLoadSizeHint(20)
        .setEnablePlaceholders(false)
        .build()

    fun getPagedHotelList(): LiveData<PagedList<HotelItem>> {
        return LivePagedListBuilder(
            sourceFactory.map { data -> data.toHotelItem() },
            config
        ).build()
    }

    fun retry() {
        sourceFactory.hotelsDataSourceLive.value?.retry()
    }

    fun getLiekedHotelList(sort: SORT, value: SORT_VAL): LiveData<PagedList<HotelItem>>? {
        return sourceDao?.getAll()
            ?.map { it.toHotelItem() }
            ?.mapByPage {
                if (sort == SORT.ASC) {
                    if(value==SORT_VAL.RATE)
                        it.sortedBy { item->item.rate }
                    else
                        it.sortedBy { item->item.timemillis }
                } else {
                    if(value==SORT_VAL.RATE)
                        it.sortedByDescending { item->item.rate }
                    else
                        it.sortedByDescending { item->item.timemillis }
                }
            }
            ?.let { LivePagedListBuilder(it, config).build() }
    }

    enum class SORT {
        ASC, DESC
    }

    enum class SORT_VAL {
        RATE, DATE

    }

}