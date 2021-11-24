package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginModel(
    var userName: String,
    val password: String
)
