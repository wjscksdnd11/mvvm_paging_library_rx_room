package com.jeon.pagingsample.data

import java.io.Serializable

data class HotelItem(
    val description: Description,
    val id: Int,
    val name: String,
    val rate: Double,
    val thumbnail: String
): Serializable

data class Description(
    val imagePath: String,
    val price: Int,
    val subject: String
): Serializable