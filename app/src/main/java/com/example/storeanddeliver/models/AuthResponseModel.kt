package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseModel(
    var isAuthorized: Boolean,
    var token: String,
    var userInfo: UserInfo,
    var loginErrorCode: Int
)