package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class AppUser(
    val id: String,
    var userName: String,
    var role: String,
    var email: String,
    var registrationDate: String,
)
