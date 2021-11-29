package com.example.storeanddeliver.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import com.example.storeanddeliver.enums.LengthUnit
import com.example.storeanddeliver.enums.TemperatureUnit
import com.example.storeanddeliver.enums.WeightUnit
import com.example.storeanddeliver.managers.UserSettingsManager
import java.util.*

class ContextUtils(base: Context) : ContextWrapper(base) {

    companion object {

        fun updateLocale(c: Context, localeToSwitchTo: Locale): ContextWrapper {
            var context = c
            val resources: Resources = context.resources
            val configuration: Configuration = resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(localeToSwitchTo)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
            } else {
                configuration.locale = localeToSwitchTo
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context = context.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
            return ContextUtils(context)
        }

        fun setLocale(resources: Resources,localeToSet: String) {
            val localeListToSet = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleList(Locale(localeToSet))
            } else {
                TODO("VERSION.SDK_INT < N")
            }
            LocaleList.setDefault(localeListToSet)

            resources.configuration.setLocales(localeListToSet)
            resources.updateConfiguration(resources.configuration, resources.displayMetrics)
            updateUnits()
        }

        private fun updateUnits() {
            when (UserSettingsManager.currentLanguage) {
                "en" -> {
                    UserSettingsManager.units.temperature = TemperatureUnit.Fahrenheit.value
                    UserSettingsManager.units.length = LengthUnit.Yards.value
                    UserSettingsManager.units.weight = WeightUnit.Pounds.value
                }
                else -> {
                    UserSettingsManager.units.temperature = TemperatureUnit.Celsius.value
                    UserSettingsManager.units.length = LengthUnit.Meters.value
                    UserSettingsManager.units.weight = WeightUnit.Kilograms.value
                }
            }
        }
    }
}