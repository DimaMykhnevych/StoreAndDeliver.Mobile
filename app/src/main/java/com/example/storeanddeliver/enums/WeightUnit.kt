package com.example.storeanddeliver.enums

enum class WeightUnit(val value: Int) {
    Kilograms(0),
    Pounds(1);
    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}