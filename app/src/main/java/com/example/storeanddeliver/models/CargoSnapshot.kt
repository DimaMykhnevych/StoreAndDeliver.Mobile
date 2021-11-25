package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class CargoSnapshot(
    val id: String,
    var cargoSessionId: String,
    var environmentSettingId: String,
    var value: Double,
    var environmentSetting: EnvironmentSetting,
)
