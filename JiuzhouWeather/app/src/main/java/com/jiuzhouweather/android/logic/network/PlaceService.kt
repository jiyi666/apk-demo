package com.jiuzhouweather.android.logic.network

import com.jiuzhouweather.android.JiuzhouWeatherApplication
import com.jiuzhouweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    /* @GET为需要访问的配置地址，@Query为动态参数 */
    @GET("v2/place?token=${JiuzhouWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String) : Call<PlaceResponse>
}