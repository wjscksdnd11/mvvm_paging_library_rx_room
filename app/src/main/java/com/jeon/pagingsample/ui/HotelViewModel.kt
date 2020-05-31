package com.jeon.pagingsample.ui

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeon.pagingsample.data.HotelItem
import com.jeon.pagingsample.data.NetworkState
import com.jeon.pagingsample.data.dto.toHotelItem
import com.jeon.pagingsample.data.repository.HotelRepository
import com.jeon.pagingsample.data.source.HotelDataSource
import com.jeon.pagingsample.data.toHotealDao
import io.reactivex.disposables.CompositeDisposable

class HotelViewModel(private val repo: HotelRepository) : ViewModel() {

    private val _moveDetailLive = MutableLiveData<Int>()
    val moveDetailLive:LiveData<Int> = _moveDetailLive
    private val compositeDisposable = CompositeDisposable()
    val query = MutableLiveData<Pair<HotelRepository.SORT,HotelRepository.FIELD>>()
    val likeList: LiveData<PagedList<HotelItem>> = Transformations.switchMap(query){
        val (sort,sortVal) = it
        repo.getLiekedHotelList(
            sort,
            sortVal
        )
    }
    val hotelList: LiveData<PagedList<HotelItem>> =  Transformations.switchMap(likeList){likeList->
        val result = repo.getPagedHotelList()
        result.value?.forEach { item ->
            val isLike = likeList.firstOrNull { it.id == item.id } != null
            item.isLike = isLike
        }
        result
    }
    private val currentQuery= Pair(HotelRepository.SORT.DESC,HotelRepository.FIELD.RATE)
    init{
        query.value = currentQuery
    }

    fun unlike(position:Int){
        val item =hotelList.value?.get(position)
        if (item != null) {
            repo.remove(item.id)
            query.value = currentQuery
            refresh()
        }
    }

    fun like(position:Int){
        val item =hotelList.value?.get(position)

        item?.also {
            repo.insert(it.toHotealDao())
            query.value = currentQuery
        }
    }

    fun retry() {
        repo.retry()
    }

    fun refresh() {
        repo.sourceFactory.hotelsDataSourceLive.value?.invalidate()
    }

    fun localDataRefresh(){
        query.value = currentQuery
    }

    fun getNetworkState(): LiveData<NetworkState> =
        Transformations.switchMap<HotelDataSource, NetworkState>(repo.sourceFactory.hotelsDataSourceLive) {
            it.networkState
        }

    fun getRefreshState(): LiveData<NetworkState> =
        Transformations.switchMap<HotelDataSource, NetworkState>(repo.sourceFactory.hotelsDataSourceLive) { it.initialLoad }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun moveDetail() {
        _moveDetailLive.value = 1
    }

}