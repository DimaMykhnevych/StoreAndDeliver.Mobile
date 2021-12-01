package com.example.storeanddeliver.models

import java.util.*

data class CargoSessionNote(
    val id: String,
    var noteCreationDate: Date,
    var content: String,
    var cargoSessionId: String
)
