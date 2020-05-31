package com.jeon.pagingsample

import java.text.SimpleDateFormat
import java.util.*

fun getTimeFormat(timemillis:Long):String{
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(Date(timemillis))
}