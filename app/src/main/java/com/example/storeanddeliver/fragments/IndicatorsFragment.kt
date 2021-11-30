package com.example.storeanddeliver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.databinding.FragmentIndicatorsBinding
import com.example.storeanddeliver.databinding.FragmentRequestsBinding
import com.example.storeanddeliver.listAdapters.IndicatorsAdapter
import com.example.storeanddeliver.listAdapters.RequestsAdapter
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
    private var _binding: FragmentIndicatorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var indicatorsView: RecyclerView

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
        _binding = FragmentIndicatorsBinding.inflate(inflater, container, false)
        binding.progressBarIndicators.isVisible = true
        binding.emptyIndicatorsText.visibility = View.GONE
        indicatorsView = binding.indicatorsView
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupIndicatorsRecyclerView() {
        indicatorsView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = IndicatorsAdapter(userCargoSnapshots, context)
        }
    }

    private val onResponse: (Call, Response) -> Unit = { _, response ->
        val responseString = response.body!!.string()
        parseResponse(responseString)
        updateView()
    }

    private fun updateView() {
        activity?.runOnUiThread {
            binding.progressBarIndicators.isVisible = false
            setupIndicatorsRecyclerView()
            if (userCargoSnapshots.size == 0) {
                binding.emptyIndicatorsText.visibility = View.VISIBLE
            } else {
                binding.emptyIndicatorsText.visibility = View.GONE
            }
        }
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