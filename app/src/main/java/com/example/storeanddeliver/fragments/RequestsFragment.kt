package com.example.storeanddeliver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.databinding.FragmentRequestsBinding
import com.example.storeanddeliver.listAdapters.RequestsAdapter
import com.example.storeanddeliver.models.CargoRequest
import com.example.storeanddeliver.models.GetOptimizedRequestModel
import com.example.storeanddeliver.models.Units
import com.example.storeanddeliver.services.CargoRequestService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Call
import okhttp3.Response

class Requests : Fragment() {
    private var cargoGroup: HashMap<String, ArrayList<CargoRequest>>? = null
    private var requests: MutableList<CargoRequest> = ArrayList()
    private var _binding: FragmentRequestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var requestsView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var cargoRequestService = CargoRequestService()
        var getModel = GetOptimizedRequestModel(
            requestType = 0,
            units = Units(
                weight = 0,
                length = 0,
                temperature = 0,
                humidity = 0,
                luminosity = 0
            ),
            currentLanguage = "en",
            status = 0
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
            binding.progressBarCyclic.isVisible = false
            requestsView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = RequestsAdapter(requests)
            }
        }
    }

    private fun parseResponse(response: String) {
        val kMapper = jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        )
        var hashMap = HashMap<String, ArrayList<CargoRequest>>()
        val typeRef = object : TypeReference<HashMap<String, ArrayList<CargoRequest>>>() {}
        cargoGroup = kMapper.readValue(response, typeRef)
        cargoGroup?.let { map ->
            requests = map.values.flatMap { it.asIterable() }.toMutableList()
            var test = requests[0].cargo?.description
        }
    }
}