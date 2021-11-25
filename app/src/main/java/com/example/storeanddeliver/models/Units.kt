package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class Units(
    var weight: Int,
    var length: Int,
    var temperature: Int,
    var humidity: Int,
    var luminosity: Int
)