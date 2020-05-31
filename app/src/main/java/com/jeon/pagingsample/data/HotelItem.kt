package com.jeon.pagingsample.data

import java.io.Serializable

data class HotelItem(
    val description: DescItem,
    val id: Int,
    val name: String,
    val rate: Double,
    val thumbnail: String,
    val timemillis:Long? = null
): Serializable

data class DescItem(
    val imagePath: String,
    val price: Int,
    val subject: String
): Serializable