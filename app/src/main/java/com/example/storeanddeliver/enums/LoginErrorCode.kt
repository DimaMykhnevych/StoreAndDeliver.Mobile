package com.example.storeanddeliver.enums

import kotlinx.serialization.Serializable

@Serializable
enum class LoginErrorCode(val value: Int) {
    InvalidUsernameOrPassword(0),
    EmailConfirmationRequired(1),
    Other(100)
}