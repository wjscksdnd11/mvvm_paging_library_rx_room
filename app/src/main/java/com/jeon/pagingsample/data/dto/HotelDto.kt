package com.jeon.pagingsample.data.dto

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