package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class Cargo(
    val id: String,
    var description: String,
    var amount: Int,
    var weight: Double,
    var length: Double,
    var width: Double,
    var height: Double,
    var cargoSettings: ArrayList<CargoSetting>?,
    var cargoRequests: ArrayList<CargoRequest>?
)
