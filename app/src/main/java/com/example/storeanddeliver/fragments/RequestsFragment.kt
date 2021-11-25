package com.example.storeanddeliver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storeanddeliver.R
import com.example.storeanddeliver.models.CargoRequest
import com.example.storeanddeliver.models.CargoRequestGroup
import com.example.storeanddeliver.models.GetOptimizedRequestModel
import com.example.storeanddeliver.models.Units
import com.example.storeanddeliver.services.CargoRequestService
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Response

class Requests : Fragment() {
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
        return inflater.inflate(R.layout.fragment_requests, container, false)
    }

    private val onResponse: (Call, Response) -> Unit = { _, response ->
        val responseString = response.body!!.string()
        parseResponse(responseString)
    }

    private fun parseResponse(response: String){
        val kMapper = ObjectMapper()
        var hashMap = HashMap<String, ArrayList<CargoRequest>>()
        var res = kMapper.readValue(response, hashMap::class.java)
    }
}