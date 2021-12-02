package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class GetCargoSnapshotModel(
    var Temperature: Int,
    var Humidity: Int,
    var Luminosity: Int
)
