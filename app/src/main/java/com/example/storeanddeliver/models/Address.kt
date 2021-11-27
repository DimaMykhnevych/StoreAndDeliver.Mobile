package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val id: String,
    var longtitude: Double,
    var latitude: Double,
    var country: String,
    var city: String,
    var street: String,
    var requestsFrom: ArrayList<Request>?,
    var requestsTo: ArrayList<Request>?,
    var store: Store?
)
