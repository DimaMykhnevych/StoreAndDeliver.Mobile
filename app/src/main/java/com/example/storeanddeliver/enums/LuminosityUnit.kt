package com.example.storeanddeliver.enums

enum class LuminosityUnit(val value: Int) {
    Lux(0);
    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}