package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val userId: String,
    var userName: String,
    var role: String,
    var registryDate: String,
    var email: String
)
