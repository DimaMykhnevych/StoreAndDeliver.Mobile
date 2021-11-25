package com.example.storeanddeliver.enums

enum class TemperatureUnit(val value: Int) {
    Celsius(0),
    Fahrenheit(1),
    Kelvin(2);
    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}