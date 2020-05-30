package com.jeon.pagingsample.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *   "id": 1001,
"name": "내 집 같은 편안한 여기어때 숙소",
"thumbnail": "https://gccompany.co.kr/App/thumbnail/thumb_img_1.jpg",
"description": {
"imagePath": "https://gccompany.co.kr/App/image/img_1.jpg",
"subject": "합리적인 가격으로 안심, 청결, 내 집 같은 편암함을 제공합니다.",
"price": 30000
},
"rate": 9.9
 */
@Entity
data class Hotel(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "image_path") val imagePath: String?,
    @ColumnInfo(name = "thumbnail") val thumbnail: String?,
    @ColumnInfo(name = "subject") val subject: String?,
    @ColumnInfo(name = "price") val price: Double?
)