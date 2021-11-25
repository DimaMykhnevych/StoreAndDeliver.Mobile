package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class EnvironmentSetting(
    val id: String,
    var name: String
)
