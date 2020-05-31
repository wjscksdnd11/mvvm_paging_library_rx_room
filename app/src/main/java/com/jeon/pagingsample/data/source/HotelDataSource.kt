package com.jeon.pagingsample.data.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.jeon.pagingsample.data.NetworkState
import com.jeon.pagingsample.data.api.HotelService
import com.jeon.pagingsample.data.dto.Product
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HotelDataSource(
    private val apiService: HotelService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Long, Product>() {
    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Product>
    ) {
        compositeDisposable.add(apiService.getHotels(1).subscribe({ hotels ->
            callback.onResult(hotels.data.product, 1, 2)
        }, {

        }))
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Product>) {
        compositeDisposable.add(apiService.getHotels(params.key).subscribe({ hotels ->
            callback.onResult(hotels.data.product, params.key+1)
        }, {

        }))
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Product>) {
    }

    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    /**
     * Keep Completable reference for the retry event
     */
    private var retryCompletable: Completable? = null

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, { throwable -> throwable.printStackTrace() }))
        }
    }
}