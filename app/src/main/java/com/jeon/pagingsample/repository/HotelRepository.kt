package com.jeon.pagingsample.repository

import com.jeon.pagingsample.data.source.DataSource



interface Repository{
    fun loadDatas()
    fun loadLikeDatas()
    fun insert()
    fun remove()
}
class HotelRepository (val dataSource: DataSource){


}