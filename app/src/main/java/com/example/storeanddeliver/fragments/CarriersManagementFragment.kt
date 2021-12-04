package com.example.storeanddeliver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.databinding.FragmentCarriersManagementBinding
import com.example.storeanddeliver.dialogs.ConfirmDeletionDialog
import com.example.storeanddeliver.listAdapters.CarriersAdapter
import com.example.storeanddeliver.models.Carrier
import com.example.storeanddeliver.services.CarrierService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Call
import okhttp3.Response

class CarriersManagementFragment : Fragment() {
    private var carriers: MutableList<Carrier> = mutableListOf()
    private var _binding: FragmentCarriersManagementBinding? = null
    private val carrierService = CarrierService()
    private val binding get() = _binding!!
    private lateinit var carriersView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carrierService.getCarriers(onResponse)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarriersManagementBinding.inflate(inflater, container, false)
        binding.carriersProgressBarIndicators.isVisible = true
        binding.emptyCarriersText.visibility = View.GONE
        carriersView = binding.carriersView
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupIndicatorsRecyclerView() {
        activity?.runOnUiThread {
            carriersView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = CarriersAdapter(carriers, context, onCarrierDelete)
            }
        }
    }

    private val onCarrierDelete: (carrier: Carrier) -> Unit = {
        var dialog = ConfirmDeletionDialog(
            it.appUser.userName,
            it.id,
            onConfirmDeletion
        )
        dialog.show(fragmentManager!!, "carrier_delete_confirmation")
    }

    private val onConfirmDeletion: (String) -> Unit = {
        carrierService.deleteCarrier(it, onDeleteCarrierResponse)
    }

    private val onDeleteCarrierResponse: (Call, Response) -> Unit = { _, _ ->
        carriers.clear()
        setupIndicatorsRecyclerView()
        activity?.runOnUiThread {
            binding.carriersProgressBarIndicators.isVisible = true
            binding.emptyCarriersText.visibility = View.GONE
        }
        carrierService.getCarriers(onResponse)
    }

    private val onResponse: (Call, Response) -> Unit = { _, response ->
        val responseString = response.body!!.string()
        parseResponse(responseString)
        updateView()
    }

    private fun parseResponse(response: String) {
        val kMapper = jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        )
        val typeRef = object : TypeReference<ArrayList<Carrier>>() {}
        carriers = kMapper.readValue(response, typeRef)
    }

    private fun updateView() {
        activity?.runOnUiThread {
            binding.carriersProgressBarIndicators.isVisible = false
            setupIndicatorsRecyclerView()
            if (carriers.size == 0) {
                binding.emptyCarriersText.visibility = View.VISIBLE
            } else {
                binding.emptyCarriersText.visibility = View.GONE
            }
        }
    }
}