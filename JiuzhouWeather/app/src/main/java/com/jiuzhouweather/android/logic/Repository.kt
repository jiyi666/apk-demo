package com.jiuzhouweather.android.logic

import androidx.lifecycle.liveData
import com.jiuzhouweather.android.logic.model.Place
import com.jiuzhouweather.android.logic.network.JiuzhouWeatherNetwork
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import java.lang.Exception
import java.lang.RuntimeException


object Repository {
    fun searchPlaces(query: String) = liveData(Dispatchers.IO){
        val result = try {
            val placeResponse =  JiuzhouWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "OK"){
                val places = placeResponse.palces
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is " +
                        "${placeResponse.status}"))
            }
        } catch (e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}