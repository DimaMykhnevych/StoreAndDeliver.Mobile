package com.example.storeanddeliver.enums

enum class HumidityUnit(val value: Int) {
    Percentage(0);
    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}