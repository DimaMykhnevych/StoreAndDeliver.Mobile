package com.example.storeanddeliver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.storeanddeliver.databinding.ActivityMapsBinding
import com.example.storeanddeliver.enums.RequestType
import com.example.storeanddeliver.models.CargoRequest
import com.example.storeanddeliver.models.Request
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var cargoRequest: CargoRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cargoRequestString = intent.getStringExtra("cargoRequest")
        cargoRequest = parseCargoRequest(cargoRequestString)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val fromAddress = cargoRequest.request!!.fromAddress!!
        val fromAddressPoint = LatLng(fromAddress.latitude, fromAddress.longtitude)
        mMap.addMarker(
            MarkerOptions().position(fromAddressPoint).title(getString(R.string.from_address))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(fromAddressPoint))
        if (cargoRequest.request?.type == RequestType.Store.value) {
            val storeAddress = cargoRequest.store!!.address!!
            val storeAddressPoint = LatLng(storeAddress.latitude, storeAddress.longtitude)
            mMap.addMarker(
                MarkerOptions().position(storeAddressPoint).title(getString(R.string.store_address))
            )
            mMap.run {

            }
        } else {
            val toAddress = cargoRequest.request!!.toAddress!!
            val toAddressPoint = LatLng(toAddress.latitude, toAddress.longtitude)
            mMap.addMarker(
                MarkerOptions().position(toAddressPoint).title(getString(R.string.to_address))
            )
        }
    }

    private fun parseCargoRequest(cargoRequest: String?): CargoRequest {
        val kMapper = jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        )
        val typeRef = object : TypeReference<CargoRequest>() {}
        return kMapper.readValue(cargoRequest, typeRef)
    }
}