package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCargoRequestModel(
    var requestGroup: HashMap<String, ArrayList<CargoRequest>>,
    var units: Units,
    var language: String
)
