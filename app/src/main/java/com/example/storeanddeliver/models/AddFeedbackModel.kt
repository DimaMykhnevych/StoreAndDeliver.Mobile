package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class AddFeedbackModel(
    val content: String,
    val rating: Float
)
