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
import com.example.storeanddeliver.models.AverageCargoSnapshots

class CargoSnapshotsAdapter(
    private val cargoSnapshots: MutableList<AverageCargoSnapshots>?,
    private val context: Context
) : RecyclerView.Adapter<CargoSnapshotsAdapter.ViewHolder>() {

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
        val cargoSnapshotsHeader: TextView = itemView.findViewById(R.id.cargo_snapshots_header)
        val snapshotValue: TextView = itemView.findViewById(R.id.snapshot_value)
        val snapshotUnit: TextView = itemView.findViewById(R.id.snapshot_unit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cargo_snapshots_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCargoSnapshot = cargoSnapshots?.get(position)
        holder.cargoSnapshotsHeader.text =
            getCargoSnapshotsHeaderText(currentCargoSnapshot?.settingName)
        holder.snapshotValue.text = getSettingValue(currentCargoSnapshot?.averageValue)
        holder.snapshotUnit.text = getSnapshotUnit(currentCargoSnapshot?.settingName)
    }

    override fun getItemCount(): Int {
        return cargoSnapshots?.size ?: 0
    }

    private fun getSnapshotUnit(settingName: String?) : String {
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

    private fun getSettingValue(snapshotAverageValue: Double?): String {
        return ": ${"%.2f".format(snapshotAverageValue)}"
    }

    private fun getCargoSnapshotsHeaderText(settingName: String?): String {
        return when (UserSettingsManager.currentLanguage) {
            "en" -> settingName ?: ""
            "ua" -> settingsDictionary[settingName]!!.ua
            "ru" -> settingsDictionary[settingName]!!.ru
            else -> settingName ?: ""
        }
    }
}