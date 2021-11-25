package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class Carrier(
    val id: String,
    var companyName: String,
    var maxCargoVolume: Double,
    var currentOccupiedVolume: Double,
    var appUserId: String,
    var appUser: AppUser,
)