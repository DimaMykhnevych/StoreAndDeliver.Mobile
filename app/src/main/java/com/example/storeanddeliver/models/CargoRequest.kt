package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class CargoRequest(
    val id: String,
    var status: Int,
    var cargoId: String,
    var requestId: String,
    var storeId: String,
    var cargo: Cargo,
    var request: Request,
    var store: Store,
)
