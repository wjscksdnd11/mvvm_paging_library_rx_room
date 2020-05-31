package com.jeon.pagingsample.data.api

import com.jeon.pagingsample.data.dto.HotelDto
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface HotelService {

    @GET("App/json/{path}.json")
    fun getHotels(@Path("path") page: Long): Single<HotelDto>

    companion object {
        private const val ALL_TIMEOUT = 10L

        fun getService(): HotelService {
            /*
             * 로깅 인터셉터 연결
             */
            val httpLogging = HttpLoggingInterceptor()
            httpLogging.level = HttpLoggingInterceptor.Level.BASIC
            val okHttpClient = OkHttpClient().newBuilder().apply {
                addInterceptor(httpLogging)
                connectTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)
                writeTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(ALL_TIMEOUT, TimeUnit.SECONDS)
            }.build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.gccompany.co.kr/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(HotelService::class.java)
        }
    }
}