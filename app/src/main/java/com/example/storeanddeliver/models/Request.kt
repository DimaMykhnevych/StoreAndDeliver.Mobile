package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val id: String,
    val requestDate: String,
    val carryOutBefore: String,
    val storeFromDate: String,
    val storeUntilDate: String,
    val type: Int,
    val isSecurityModeEnabled: Boolean,
    val totalSum: Double,
    val fromAddress: Address,
    val toAddress: Address,
)
