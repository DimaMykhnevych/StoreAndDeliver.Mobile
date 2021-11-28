package com.example.storeanddeliver.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.storeanddeliver.R
import com.example.storeanddeliver.managers.UserSettingsManager

class UnitsDialog(onUnitsConfigured: () -> Unit) : DialogFragment() {
    private lateinit var lengthUnitSpinner: Spinner
    private lateinit var weightUnitSpinner: Spinner
    private lateinit var temperatureUnitSpinner: Spinner
    private lateinit var humidityUnitSpinner: Spinner
    private lateinit var luminosityUnitSpinner: Spinner
    private var callback: () -> Unit = onUnitsConfigured

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.unit_settings, null)
            setupSpinners(view)
            configureSpinners()
            setupInitialSpinnerValues()
            builder.setView(view)
                .setPositiveButton(
                    R.string.apply_units
                ) { _, _ ->
                    saveUnits()
                    callback()
                }
                .setNegativeButton(
                    R.string.cancel
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun saveUnits() {
        UserSettingsManager.units.length = lengthUnitSpinner.selectedItemPosition
        UserSettingsManager.units.weight = weightUnitSpinner.selectedItemPosition
        UserSettingsManager.units.temperature = temperatureUnitSpinner.selectedItemPosition
        UserSettingsManager.units.humidity = humidityUnitSpinner.selectedItemPosition
        UserSettingsManager.units.luminosity = luminosityUnitSpinner.selectedItemPosition
    }

    private fun setupInitialSpinnerValues() {
        lengthUnitSpinner.setSelection(UserSettingsManager.units.length)
        weightUnitSpinner.setSelection(UserSettingsManager.units.weight)
        temperatureUnitSpinner.setSelection(UserSettingsManager.units.temperature)
        humidityUnitSpinner.setSelection(UserSettingsManager.units.humidity)
        luminosityUnitSpinner.setSelection(UserSettingsManager.units.luminosity)
    }

    private fun setupSpinners(view: View) {
        lengthUnitSpinner = view.findViewById(R.id.length_unit_spinner)
        weightUnitSpinner = view.findViewById(R.id.weight_unit_spinner)
        temperatureUnitSpinner = view.findViewById(R.id.temperature_unit_spinner)
        humidityUnitSpinner = view.findViewById(R.id.humidity_unit_spinner)
        luminosityUnitSpinner = view.findViewById(R.id.luminosity_unit_spinner)
    }

    private fun configureSpinners() {
        configureSpinner(R.array.length_units_array, lengthUnitSpinner)
        configureSpinner(R.array.weight_units_array, weightUnitSpinner)
        configureSpinner(R.array.temperature_units_array, temperatureUnitSpinner)
        configureSpinner(R.array.humidity_units_array, humidityUnitSpinner)
        configureSpinner(R.array.luminosity_units_array, luminosityUnitSpinner)
    }

    private fun configureSpinner(resourceId: Int, spinner: Spinner) {
        ArrayAdapter.createFromResource(
            context!!,
            resourceId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}