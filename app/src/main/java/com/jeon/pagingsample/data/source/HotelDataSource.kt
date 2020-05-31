package com.jeon.pagingsample.data.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.jeon.pagingsample.data.NetworkState
import com.jeon.pagingsample.data.api.HotelService
import com.jeon.pagingsample.data.dto.Product
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class HotelDataSource(
    private val apiService: HotelService,
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Long, Product>() {
    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, Product>
    ) {
        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)
        compositeDisposable.add(apiService.getHotels(1)
            .subscribe({ hotels ->
                setRetry(null)
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                hotels.data?.product?.let {
                    callback.onResult(it, 1, 2)
                }
            }, {
                setRetry(Action { loadInitial(params, callback) })
                val error = NetworkState.error(it.message)
                networkState.postValue(error)
                initialLoad.postValue(error)
                it.printStackTrace()
            })
        )
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Product>) {
        networkState.postValue(NetworkState.LOADING)
        compositeDisposable.add(apiService.getHotels(params.key).subscribe({ hotels ->
            setRetry(null)
            networkState.postValue(NetworkState.LOADED)
            if (hotels.data?.product != null)
                callback.onResult(hotels.data.product, params.key + 1)
        }, {
            it.printStackTrace()

        }))
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Product>) {
    }

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    private var retryCompletable: Completable? = null

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ }, { throwable -> throwable.printStackTrace() })
            )
        }
    }

    private fun setRetry(action: Action?) {
        if (action == null) {
            this.retryCompletable = null
        } else {
            this.retryCompletable = Completable.fromAction(action)
        }
    }
}
