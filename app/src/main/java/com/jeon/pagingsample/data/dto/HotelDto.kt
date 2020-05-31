package com.jeon.pagingsample.data.dto

import com.jeon.pagingsample.data.HotelItem
import java.io.Serializable

data class HotelDto(
    val code: Int,
    val `data`: Data,
    val msg: String
):Serializable

data class Data(
    val product: List<Product>,
    val totalCount: Int
):Serializable

data class Product(
    val description: Description,
    val id: Int,
    val name: String,
    val rate: Double,
    val thumbnail: String
):Serializable

data class Description(
    val imagePath: String,
    val price: Int,
    val subject: String
):Serializable

fun Description.toHotelDescritipn():com.jeon.pagingsample.data.DescItem{
    return com.jeon.pagingsample.data.DescItem(imagePath, price, subject)
}
fun Product.toHotelItem():HotelItem{
    return HotelItem(description = description.toHotelDescritipn(),id = id,name = name,rate = rate,thumbnail = thumbnail)
}