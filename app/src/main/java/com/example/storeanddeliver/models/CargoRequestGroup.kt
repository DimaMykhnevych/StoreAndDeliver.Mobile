package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class CargoRequestGroup(
    val requests: Map<String, ArrayList<CargoRequest>>
)
