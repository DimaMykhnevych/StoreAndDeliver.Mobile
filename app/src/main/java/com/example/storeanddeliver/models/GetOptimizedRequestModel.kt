package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class GetOptimizedRequestModel(
var requestType: Int,
var units: Units,
var currentLanguage: String,
var status: Int
)
