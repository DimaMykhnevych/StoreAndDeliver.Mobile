package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: String,
    var longtitude: Double,
    var latitude: Double,
    var country: String,
    var cityName: String,
)
