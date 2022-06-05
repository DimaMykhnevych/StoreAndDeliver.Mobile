package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class Feedback(
    val id: String,
    val content: String,
    val rating: Float,
    val date: String?,
    val username: String?,
    val userEmail: String?
)
