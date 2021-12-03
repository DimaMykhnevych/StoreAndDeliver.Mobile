package com.example.storeanddeliver.models

import kotlinx.serialization.Serializable

@Serializable
data class AddCargoSessionNoteModel(
    var id: String,
    var noteCreationDate: String,
    var content: String,
    var cargoRequestId: String
)
