package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class CargoSetting(
    val id: String,
    var minValue:Double,
    var maxValue: Double,
    var cargoId: String,
    var environmentSettingId: String,
    var cargo: Cargo?,
    var environmentSetting: EnvironmentSetting?
)
