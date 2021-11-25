package com.example.storeanddeliver.enums

enum class UnitType(val value: Int) {
    Weight(0),
    Length(1),
    Temperature(2),
    Humidity(3),
    Luminosity(4),
    Other(100);
    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}