package com.jeon.pagingsample

import com.jeon.pagingsample.data.api.HotelService
import io.reactivex.schedulers.Schedulers
import org.junit.Test

class ApiTest{
    @Test
    fun gethotels() {
        HotelService
            .getService()
            .getHotels(1)
            .subscribeOn(Schedulers.trampoline())
            .subscribe(
                {println(it.toString())},
                {it.printStackTrace()})

    }
}