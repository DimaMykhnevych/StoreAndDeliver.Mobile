package com.example.storeanddeliver.models

data class UserCargoSnapshots(
    val cargo: Cargo?,
    val cargoSnapshots: ArrayList<CargoSnapshot>?
)
