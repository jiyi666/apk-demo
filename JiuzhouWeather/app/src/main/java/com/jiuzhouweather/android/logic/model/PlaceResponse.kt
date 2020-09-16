package com.jiuzhouweather.android.logic.model

import com.google.gson.annotations.SerializedName

data class Location(val lng: String, val lat: String)

/* @SerializedName注释是为了解决kotlin成员对象与json字段不匹配问题 */
data class Place(val name: String, val location: Location,
                @SerializedName("formatted_address") val address: String)

data class PlaceResponse(val status: String, val palces: List<Place>)