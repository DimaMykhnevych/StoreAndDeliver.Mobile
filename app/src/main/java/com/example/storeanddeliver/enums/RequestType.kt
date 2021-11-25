package com.example.storeanddeliver.enums

enum class RequestType(val value: Int) {
    Store(0),
    Deliver(1),
    Other(100);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}