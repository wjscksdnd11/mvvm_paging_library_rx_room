package com.jeon.pagingsample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.jeon.pagingsample.data.HotelItem
import com.jeon.pagingsample.data.NetworkState
import com.jeon.pagingsample.data.repository.HotelRepository
import com.jeon.pagingsample.data.source.HotelDataSource
import com.jeon.pagingsample.data.toHotealDao
import io.reactivex.disposables.CompositeDisposable

class HotelViewModel(private val repo: HotelRepository) : ViewModel() {

    private val _moveDetailLive = MutableLiveData<Int>()
    val moveDetailLive: LiveData<Int> = _moveDetailLive
    private val compositeDisposable = CompositeDisposable()
    val query = MutableLiveData<Pair<HotelRepository.SORT, HotelRepository.FIELD>>()
    val likeList: LiveData<PagedList<HotelItem>> = Transformations.switchMap(query) {
        val (sort, sortVal) = it
        val list = repo.getLiekedHotelList(
            sort,
            sortVal
        )
        list
    }
    private val _hotelList: LiveData<PagedList<HotelItem>> = repo.getPagedHotelList()
    val hotelList: LiveData<PagedList<HotelItem>> = Transformations.map(_hotelList) {
        it.map { item ->
            val isLiked =
                likeList.value?.firstOrNull { likedItem -> likedItem.id == item.id } != null
            item.isLike = isLiked
        }
        it
    }
    val detailItem = MutableLiveData<HotelItem>()
    private var currentQuery = Pair(HotelRepository.SORT.DESC, HotelRepository.FIELD.RATE)

    init {
        query.value = currentQuery
    }

    fun changeQuery(
        sort: HotelRepository.SORT = currentQuery.first,
        value: HotelRepository.FIELD = currentQuery.second
    ) {
        query.value = Pair(sort, value)
    }

    fun unlike(position: Int) {
        likeList.value?.let {
            if (it.size > 0) {
                val item = likeList.value?.get(position)
                if (item != null) {
                    repo.remove(item.id)
                    query.value = currentQuery
                    refresh()
                }
            }
        }
    }

    fun like(position: Int) {
        val item = hotelList.value?.get(position)
        item?.also {
            repo.insert(it.toHotealDao())
        }
    }

    fun likebyId(id: Int) {
        val item = hotelList.value?.firstOrNull { it.id == id }
        item?.also {
            repo.insert(it.toHotealDao())
        }
    }

    fun unlikebyId(id: Int) {
        repo.remove(id)
    }

    fun retry() {
        repo.retry()
    }

    fun refresh() {
        repo.sourceFactory.hotelsDataSourceLive.value?.invalidate()
    }

    fun localDataRefresh() {
        query.value = currentQuery
    }

    fun getNetworkState(): LiveData<NetworkState> =
        Transformations.switchMap<HotelDataSource, NetworkState>(repo.sourceFactory.hotelsDataSourceLive) {
            it.networkState
        }

    fun getRefreshState(): LiveData<NetworkState> =
        Transformations.switchMap<HotelDataSource, NetworkState>(repo.sourceFactory.hotelsDataSourceLive) {
            it.initialLoad
        }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun moveDetail(id: Int) {
        val item = hotelList.value?.firstOrNull { it.id == id }
        item?.also {
            detailItem.value = it
            _moveDetailLive.value = 1
        }
    }

}