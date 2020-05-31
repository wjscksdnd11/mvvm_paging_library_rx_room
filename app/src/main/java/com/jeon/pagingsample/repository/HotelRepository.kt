package com.jeon.pagingsample.repository

import com.jeon.pagingsample.data.api.HotelService
import com.jeon.pagingsample.data.source.DataSource
import com.jeon.pagingsample.data.source.HotelsDataSourceFactory
import io.reactivex.disposables.CompositeDisposable


interface Repository{
    fun loadDatas()
    fun loadLikeDatas()
    fun insert()
    fun remove()
}
class HotelRepository (val dataSource: DataSource){
    private val compositeDisposable = CompositeDisposable()
    private val sourceFactory =
        HotelsDataSourceFactory(compositeDisposable, HotelService.getService())

}