package com.jeon.pagingsample.data.source

import androidx.paging.PageKeyedDataSource
import com.jeon.pagingsample.data.api.HotelService
import com.jeon.pagingsample.data.dto.HotelDto
import io.reactivex.disposables.CompositeDisposable

class HotelDataSource(
    private val apiService: HotelService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Long, HotelDto>() {
    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, HotelDto>
    ) {
        compositeDisposable.add(apiService.getHotels(1).subscribe({ hotels ->
            callback.onResult(hotels, 1, 2)
        }, {

        }))
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, HotelDto>) {
        compositeDisposable.add(apiService.getHotels(params.key).subscribe({ hotels ->
            callback.onResult(hotels, params.key+1)
        }, {

        }))
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, HotelDto>) {
    }

}