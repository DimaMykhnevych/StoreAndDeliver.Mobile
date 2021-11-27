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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.databinding.FragmentRequestsBinding
import com.example.storeanddeliver.enums.RequestStatus
import com.example.storeanddeliver.enums.RequestType
import com.example.storeanddeliver.listAdapters.RequestsAdapter
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.models.CargoRequest
import com.example.storeanddeliver.models.GetOptimizedRequestModel
import com.example.storeanddeliver.models.Units
import com.example.storeanddeliver.services.CargoRequestService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Call
import okhttp3.Response

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
        configureRequestTypeSpinner()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var getModel = GetOptimizedRequestModel(
            requestType = if(position == 1) 0 else 1,
            units = UserSettingsManager.units,
            currentLanguage = UserSettingsManager.currentLanguage,
            status = requestStatus.value
        )
        requests.clear()
        binding.progressBarCyclic.isVisible = true
        cargoRequestService.getUserCargoRequests(getModel, onResponse)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private val onResponse: (Call, Response) -> Unit = { _, response ->
        val responseString = response.body!!.string()
        parseResponse(responseString)
        updateView()
    }

    private fun updateView() {
        activity?.runOnUiThread {
            binding.progressBarCyclic.isVisible = false
            requestsView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = RequestsAdapter(requests, context)
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
        cargoGroup?.let { map ->
            requests = map.values.flatMap { it.asIterable() }.toMutableList()
        }
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