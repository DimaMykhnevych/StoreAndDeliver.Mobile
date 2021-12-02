package com.example.storeanddeliver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.isVisible
import com.example.storeanddeliver.R
import com.example.storeanddeliver.databinding.FragmentCarrierRequestsBinding
import com.example.storeanddeliver.dialogs.UnitsDialog
import com.example.storeanddeliver.enums.RequestStatus
import com.example.storeanddeliver.enums.RequestType
import com.example.storeanddeliver.listeners.RequestStatusSpinnerListener
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.models.CargoRequest
import com.example.storeanddeliver.models.GetOptimizedRequestModel
import com.example.storeanddeliver.services.CargoSessionService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Call
import okhttp3.Response

class CarrierRequestsFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private var cargoGroup: HashMap<String, ArrayList<CargoRequest>> = HashMap()
    private var _binding: FragmentCarrierRequestsBinding? = null
    private val binding get() = _binding!!
    private var requestType: RequestType = RequestType.Deliver
    private var requestStatus: RequestStatus = RequestStatus.InProgress
    private var cargoSessionService = CargoSessionService()
    private lateinit var requestTypesSpinner: Spinner
    private lateinit var requestStatusSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var getModel = GetOptimizedRequestModel(
            requestType = requestType.value,
            units = UserSettingsManager.units,
            currentLanguage = UserSettingsManager.currentLanguage,
            status = requestStatus.value
        )
        cargoSessionService.getCarrierRequests(getModel, onResponse)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCarrierRequestsBinding.inflate(inflater, container, false)
        binding.carrierRequestsProgressBarCyclic.isVisible = true
        requestTypesSpinner = binding.carrierRequestTypeSpinner
        requestStatusSpinner = binding.carrierRequestStatusSpinner
        binding.btnUnitsSettingsCarrier.setOnClickListener { onUnitSettingsButtonClick() }
        configureRequestTypeSpinner()
        configureRequestStatusSpinner()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val onResponse: (Call, Response) -> Unit = { _, response ->
        val responseString = response.body!!.string()
        parseResponse(responseString)
        updateView()
    }

    private fun updateView() {
        activity?.runOnUiThread {
            binding.carrierRequestsProgressBarCyclic.isVisible = false
//            setupRequestsRecyclerView()
            if (cargoGroup.isEmpty()) {
                binding.emptyCarrierRequestsText.visibility = View.VISIBLE
            } else {
                binding.emptyCarrierRequestsText.visibility = View.GONE
            }
        }
    }

    private fun parseResponse(response: String) {
        val kMapper = jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        )
        val typeRef = object : TypeReference<HashMap<String, ArrayList<CargoRequest>>>() {}
        cargoGroup = kMapper.readValue(response, typeRef)
    }

    private fun onUnitSettingsButtonClick() {
        var dialog = UnitsDialog(onUnitsConfigured)
        dialog.show(fragmentManager!!, "units_settings")
    }

    private val onUnitsConfigured: () -> Unit = {
        updateRequestsList()
    }

    private val onRequestStatusSelected: (AdapterView<*>?, View?, Int, Long) -> Unit =
        { _, _, position, _ ->
            requestStatus = when (position) {
                0 -> RequestStatus.InProgress
                1 -> RequestStatus.Completed
                else -> RequestStatus.InProgress
            }
            updateRequestsList()
        }

    private fun updateRequestsList() {
        var getModel = GetOptimizedRequestModel(
            requestType = requestType.value,
            units = UserSettingsManager.units,
            currentLanguage = UserSettingsManager.currentLanguage,
            status = requestStatus.value
        )
        cargoGroup.clear()
//        setupRequestsRecyclerView()
        binding.carrierRequestsProgressBarCyclic.isVisible = true
        binding.emptyCarrierRequestsText.visibility = View.GONE
        cargoSessionService.getCarrierRequests(getModel, onResponse)
    }

    private fun configureRequestStatusSpinner() {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.carrier_request_status_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            requestStatusSpinner.adapter = adapter
        }
        requestStatusSpinner.onItemSelectedListener =
            RequestStatusSpinnerListener(onRequestStatusSelected)
    }

    private fun configureRequestTypeSpinner() {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.request_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            requestTypesSpinner.adapter = adapter
        }
        requestTypesSpinner.onItemSelectedListener = this
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        requestType = if (position == 1) RequestType.fromInt(0) else RequestType.fromInt(1)
        updateRequestsList()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}