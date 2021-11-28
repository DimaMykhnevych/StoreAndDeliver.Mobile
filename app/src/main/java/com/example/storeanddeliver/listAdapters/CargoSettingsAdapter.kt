package com.example.storeanddeliver.listAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.enums.TemperatureUnit
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.models.CargoSetting

class CargoSettingsAdapter(
    private val settings: MutableList<CargoSetting>?,
    private val context: Context
) :
    RecyclerView.Adapter<CargoSettingsAdapter.ViewHolder>() {

    data class SettingsTranslations(
        val ua: String,
        val ru: String
    )

    private var settingsDictionary: HashMap<String, SettingsTranslations> =
        hashMapOf(
            "Temperature" to SettingsTranslations("Температура", "Температура"),
            "Humidity" to SettingsTranslations("Вологість", "Влажность"),
            "Luminosity" to SettingsTranslations("Освітленність", "Освещенность")
        )

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val settingHeader: TextView = itemView.findViewById(R.id.settings_header)
        val settingValue: TextView = itemView.findViewById(R.id.setting_value)
        val settingUnit: TextView = itemView.findViewById(R.id.setting_unit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cargo_setting_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSetting = settings?.get(position)
        holder.settingHeader.text = getSettingsHeaderText(currentSetting?.environmentSetting?.name)
        holder.settingValue.text = getSettingValue(currentSetting)
        holder.settingUnit.text = getSettingUnit(currentSetting?.environmentSetting?.name)
    }

    override fun getItemCount(): Int {
        return if (settings?.size != null) settings.size else 0
    }

    private fun getSettingUnit(settingName: String?) : String {
        return when(settingName){
            "Temperature" -> when(UserSettingsManager.units.temperature) {
                TemperatureUnit.Celsius.value -> context.getString(R.string.celsius)
                TemperatureUnit.Fahrenheit.value -> context.getString(R.string.fahrenheit)
                TemperatureUnit.Kelvin.value -> context.getString(R.string.kelvin)
                else -> context.getString(R.string.celsius)
            }
            "Humidity" -> context.getString(R.string.percentage)
            "Luminosity" -> context.getString(R.string.lux)
            else -> ""
        }
    }

    private fun getSettingValue(setting: CargoSetting?): String {
        var minValue = setting?.minValue
        var maxValue = setting?.maxValue
        return ": ${"%.2f".format(minValue)} ... ${"%.2f".format(maxValue)}"
    }

    private fun getSettingsHeaderText(settingName: String?): String {
        return when (UserSettingsManager.currentLanguage) {
            "en" -> settingName ?: ""
            "ua" -> settingsDictionary[settingName]!!.ua
            "ru" -> settingsDictionary[settingName]!!.ru
            else -> settingName ?: ""
        }
    }
}