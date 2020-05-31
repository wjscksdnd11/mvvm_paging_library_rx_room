package com.jeon.pagingsample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.jeon.pagingsample.data.HotelItem
import com.jeon.pagingsample.data.NetworkState
import com.jeon.pagingsample.data.entity.Hotel
import com.jeon.pagingsample.data.repository.HotelRepository
import com.jeon.pagingsample.data.source.HotelDataSource
import io.reactivex.disposables.CompositeDisposable

class HotelViewModel(private val repo: HotelRepository) : ViewModel() {


    private val compositeDisposable = CompositeDisposable()
    val hotelList: LiveData<PagedList<HotelItem>> =repo.getPagedHotelList()
    val likeList : LiveData<PagedList<HotelItem>> = repo.getLiekedHotelList()
    fun retry() {
      repo.retry()
    }

    fun refresh() {
        repo.sourceFactory.hotelsDataSourceLive.value?.invalidate()
    }

    fun getNetworkState(): LiveData<NetworkState> =
        Transformations.switchMap<HotelDataSource, NetworkState>(repo.sourceFactory.hotelsDataSourceLive) { it.networkState }

    fun getRefreshState(): LiveData<NetworkState> =
        Transformations.switchMap<HotelDataSource, NetworkState>(repo.sourceFactory.hotelsDataSourceLive) { it.initialLoad }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}