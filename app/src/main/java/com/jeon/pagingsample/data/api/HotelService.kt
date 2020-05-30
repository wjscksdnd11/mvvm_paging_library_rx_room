package com.jeon.pagingsample.data.api

import com.jeon.pagingsample.data.dto.HotelDto
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface HotelService {

    @GET("App/json/{path}.json")
    fun getHotels(@Path("path") page: Long): Single<List<HotelDto>>

    companion object {
        fun getService(): HotelService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.gccompany.co.kr")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(HotelService::class.java)
        }
    }
}