package com.example.storeanddeliver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.databinding.FragmentStoreManagementBinding
import com.example.storeanddeliver.dialogs.ConfirmDeletionDialog
import com.example.storeanddeliver.listAdapters.StoresAdapter
import com.example.storeanddeliver.models.Store
import com.example.storeanddeliver.services.StoreService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Call
import okhttp3.Response

class StoreManagementFragment : Fragment() {
    private var stores: MutableList<Store> = mutableListOf()
    private var _binding: FragmentStoreManagementBinding? = null
    private val binding get() = _binding!!
    private val storeService = StoreService()
    private lateinit var storesView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeService.getStores(onGetStoresResponse)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoreManagementBinding.inflate(inflater, container, false)
        binding.storesProgressBarIndicators.isVisible = true
        binding.emptyStoresText.visibility = View.GONE
        storesView = binding.storesView
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupStoresRecyclerView() {
        activity?.runOnUiThread {
            storesView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = StoresAdapter(stores, context, onStoreDelete)
            }
        }
    }

    private val onStoreDelete: (store: Store) -> Unit = {
        var dialog = ConfirmDeletionDialog(
            it.name,
            it.id,
            onConfirmDeletion
        )
        dialog.show(fragmentManager!!, "carrier_delete_confirmation")
    }


    private val onConfirmDeletion: (String) -> Unit = {
        storeService.deleteStore(it, onDeleteStoreResponse)
    }

    private val onDeleteStoreResponse: (Call, Response) -> Unit = { _, _ ->
        stores.clear()
        setupStoresRecyclerView()
        activity?.runOnUiThread {
            binding.storesProgressBarIndicators.isVisible = true
            binding.emptyStoresText.visibility = View.GONE
        }
        storeService.getStores(onGetStoresResponse)
    }

    private val onGetStoresResponse: (Call, Response) -> Unit = { _, response ->
        val responseString = response.body!!.string()
        parseResponse(responseString)
        updateView()
    }

    private fun parseResponse(response: String) {
        val kMapper = jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        )
        val typeRef = object : TypeReference<ArrayList<Store>>() {}
        stores = kMapper.readValue(response, typeRef)
    }

    private fun updateView() {
        activity?.runOnUiThread {
            binding.storesProgressBarIndicators.isVisible = false
            setupStoresRecyclerView()
            if (stores.size == 0) {
                binding.emptyStoresText.visibility = View.VISIBLE
            } else {
                binding.emptyStoresText.visibility = View.GONE
            }
        }
    }
}