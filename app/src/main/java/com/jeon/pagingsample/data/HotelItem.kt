package com.jeon.pagingsample.data

import com.jeon.pagingsample.data.entity.Hotel
import java.io.Serializable

data class HotelItem(
    val description: DescItem,
    val id: Int,
    val name: String,
    val rate: Double,
    val thumbnail: String,
    val timemillis:Long =0,
    var isLike:Boolean = false
): Serializable

data class DescItem(
    val imagePath: String,
    val price: Int,
    val subject: String
): Serializable


fun HotelItem.toHotealDao(): Hotel {
    return Hotel(id = id.toString(),
        subject = description.subject,
        price = description.price.toDouble(),
        imagePath = description.imagePath,
        name = name,
        rate = rate,
        thumbnail = thumbnail,
        timemillis = System.currentTimeMillis())
}