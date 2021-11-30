package com.example.storeanddeliver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storeanddeliver.R
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.models.CargoRequest
import com.example.storeanddeliver.models.GetCargoSnapshotModel
import com.example.storeanddeliver.models.UserCargoSnapshots
import com.example.storeanddeliver.services.CargoSnapshotsService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Call
import okhttp3.Response

class IndicatorsFragment : Fragment() {
    private var userCargoSnapshots:MutableList<UserCargoSnapshots> = mutableListOf()
    private var cargoSnapshotsService = CargoSnapshotsService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var getModel = GetCargoSnapshotModel(
            Temperature = UserSettingsManager.units.temperature,
            Humidity = UserSettingsManager.units.humidity,
            Luminosity = UserSettingsManager.units.luminosity
        )
        cargoSnapshotsService.getUserCargoSnapshots(getModel, onResponse)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_indicators, container, false)
    }

    private val onResponse: (Call, Response) -> Unit = { _, response ->
        val responseString = response.body!!.string()
        parseResponse(responseString)
//        updateView()
    }

    private fun parseResponse(response: String) {
        val kMapper = jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        )
        val typeRef = object : TypeReference<ArrayList<UserCargoSnapshots>>() {}
        userCargoSnapshots = kMapper.readValue(response, typeRef)
    }
}