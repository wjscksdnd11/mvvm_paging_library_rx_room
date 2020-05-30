package com.jeon.pagingsample.data.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.jeon.pagingsample.data.api.HotelService
import com.jeon.pagingsample.data.dto.HotelDto
import io.reactivex.disposables.CompositeDisposable

class HotelsDataSourceFactory(private val compositeDisposable: CompositeDisposable,val apiService: HotelService)
    : DataSource.Factory<Long, HotelDto>() {
    val hotelsDataSourceLive = MutableLiveData<HotelDataSource>()
    override fun create(): DataSource<Long, HotelDto> {
        val hotelDataSource = HotelDataSource(apiService, compositeDisposable)
        hotelsDataSourceLive.postValue(hotelDataSource)
        return hotelDataSource
    }

}