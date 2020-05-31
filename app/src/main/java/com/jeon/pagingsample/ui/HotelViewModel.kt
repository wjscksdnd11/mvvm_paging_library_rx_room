package com.jeon.pagingsample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeon.pagingsample.data.HotelItem
import com.jeon.pagingsample.data.NetworkState
import com.jeon.pagingsample.data.api.HotelService
import com.jeon.pagingsample.data.dto.toHotelItem
import com.jeon.pagingsample.data.source.HotelDataSource
import com.jeon.pagingsample.data.source.HotelsDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class HotelViewModel : ViewModel() {


    private val compositeDisposable = CompositeDisposable()

    private val pageSize = 20
    private val sourceFactory =
        HotelsDataSourceFactory(compositeDisposable, HotelService.getService())
    private val config = PagedList.Config.Builder()
        .setPageSize(pageSize)
        .setInitialLoadSizeHint(pageSize)
        .setEnablePlaceholders(false)
        .build()
    val hotelList: LiveData<PagedList<HotelItem>> =
        LivePagedListBuilder(sourceFactory.map { it.toHotelItem() }, config).build()

    fun retry() {
        sourceFactory.hotelsDataSourceLive.value?.retry()
    }

    fun refresh() {
        sourceFactory.hotelsDataSourceLive.value?.invalidate()
    }

    fun getNetworkState(): LiveData<NetworkState> =
        Transformations.switchMap<HotelDataSource, NetworkState>(sourceFactory.hotelsDataSourceLive) { it.networkState }

    fun getRefreshState(): LiveData<NetworkState> =
        Transformations.switchMap<HotelDataSource, NetworkState>(sourceFactory.hotelsDataSourceLive
        ) { it.initialLoad }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}