package com.example.storeanddeliver.enums

enum class LengthUnit(val value: Int) {
    Meters(0),
    Yards(1);
    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}