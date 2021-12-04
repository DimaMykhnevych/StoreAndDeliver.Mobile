package com.example.storeanddeliver.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.MapsActivity
import com.example.storeanddeliver.R
import com.example.storeanddeliver.databinding.FragmentRequestsBinding
import com.example.storeanddeliver.dialogs.UnitsDialog
import com.example.storeanddeliver.enums.RequestStatus
import com.example.storeanddeliver.enums.RequestType
import com.example.storeanddeliver.listAdapters.RequestsAdapter
import com.example.storeanddeliver.listeners.RequestStatusSpinnerListener
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.models.CargoRequest
import com.example.storeanddeliver.models.GetOptimizedRequestModel
import com.example.storeanddeliver.services.CargoRequestService
import com.example.storeanddeliver.utils.ContextUtils
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Call
import okhttp3.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Requests : Fragment(), AdapterView.OnItemSelectedListener {
    private var cargoGroup: HashMap<String, ArrayList<CargoRequest>>? = null
    private var requests: MutableList<CargoRequest> = ArrayList()
    private var _binding: FragmentRequestsBinding? = null
    private var requestType: RequestType = RequestType.Deliver
    private var requestStatus: RequestStatus = RequestStatus.Pending
    private var cargoRequestService = CargoRequestService()
    private val binding get() = _binding!!
    private lateinit var requestsView: RecyclerView
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
        cargoRequestService.getUserCargoRequests(getModel, onResponse)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRequestsBinding.inflate(inflater, container, false)
        binding.progressBarCyclic.isVisible = true
        requestsView = binding.requestsView
        requestTypesSpinner = binding.requestTypeSpinner
        requestStatusSpinner = binding.requestStatusSpinner
        binding.btnUnitsSettings.setOnClickListener { onUnitSettingsButtonClick() }
        configureRequestTypeSpinner()
        configureRequestStatusSpinner()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        requestType = if (position == 1) RequestType.fromInt(0) else RequestType.fromInt(1)
        updateRequestsList()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private val onRequestStatusSelected: (AdapterView<*>?, View?, Int, Long) -> Unit =
        { _, _, position, _ ->
            requestStatus = when (position) {
                0 -> RequestStatus.Pending
                1 -> RequestStatus.InProgress
                2 -> RequestStatus.Completed
                else -> RequestStatus.Pending
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
        requests.clear()
        setupRequestsRecyclerView()
        binding.progressBarCyclic.isVisible = true
        binding.emptyRequestsText.visibility = View.GONE
        cargoRequestService.getUserCargoRequests(getModel, onResponse)
    }

    private fun onUnitSettingsButtonClick() {
        var dialog = UnitsDialog(onUnitsConfigured)
        dialog.show(fragmentManager!!, "units_settings")
    }

    private val onUnitsConfigured: () -> Unit = {
        updateRequestsList()
    }

    private val onResponse: (Call, Response) -> Unit = { _, response ->
        val responseString = response.body!!.string()
        parseResponse(responseString)
        updateView()
    }

    private fun updateView() {
        activity?.runOnUiThread {
            binding.progressBarCyclic.isVisible = false
            setupRequestsRecyclerView()
            if (requests.size == 0) {
                binding.emptyRequestsText.visibility = View.VISIBLE
            } else {
                binding.emptyRequestsText.visibility = View.GONE
            }
        }
    }

    private fun setupRequestsRecyclerView() {
        requestsView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter =
                RequestsAdapter(requests, context, fragmentManager!!, activity!!, null)
        }
    }

    private fun parseResponse(response: String) {
        val kMapper = jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        )
        val typeRef = object : TypeReference<HashMap<String, ArrayList<CargoRequest>>>() {}
        cargoGroup = kMapper.readValue(response, typeRef)
        cargoGroup?.let { map ->
            requests = map.values.flatMap { it.asIterable() }.toMutableList()
        }
    }

    private fun configureRequestStatusSpinner() {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.request_status_array,
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
}