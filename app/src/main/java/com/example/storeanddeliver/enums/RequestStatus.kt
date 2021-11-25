package com.example.storeanddeliver.enums

enum class RequestStatus(val value: Int) {
    Pending(0),
    InProgress(1),
    Rejected(2),
    Completed(3),
    Other(100);
    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}