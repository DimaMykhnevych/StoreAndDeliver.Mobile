package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class Store(
    val id: String,
    var name: String,
    var maxCargoVolume: Double,
    var currentOccupiedVolume: Double,
    var addressId: String,
    var address: Address?,
    var cargoRequests: ArrayList<CargoRequest>?
)
