package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val id: String,
    var requestDate: String,
    var carryOutBefore: String?,
    var storeFromDate: String?,
    var storeUntilDate: String?,
    var type: Int,
    var isSecurityModeEnabled: Boolean,
    var totalSum: Double,
    var fromAddressId: String?,
    var toAddressId: String?,
    var appUserId: String,
    var appUser: AppUser?,
    var fromAddress: Address?,
    var toAddress: Address?,
    var cargoRequests: ArrayList<CargoRequest>?
)
