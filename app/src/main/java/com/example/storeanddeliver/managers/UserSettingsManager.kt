package com.example.storeanddeliver.managers

import com.example.storeanddeliver.models.Units

object UserSettingsManager {
    var units = Units(
        weight = 0,
        length = 0,
        temperature = 0,
        humidity = 0,
        luminosity = 0
    );
    var currentLanguage = "en"
}