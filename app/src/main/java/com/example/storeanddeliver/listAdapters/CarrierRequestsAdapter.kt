package com.example.storeanddeliver.listAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.models.CargoRequest

class CarrierRequestsAdapter(
    private val cargoRequests: HashMap<String, ArrayList<CargoRequest>>,
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity
) :
    RecyclerView.Adapter<CarrierRequestsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carrierRequestsView: RecyclerView = itemView.findViewById(R.id.carrierRequestsView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.carrier_cargo_request_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keys = cargoRequests.keys.toMutableList()
        setupCarrierRequestsView(holder, cargoRequests[keys[position]]!!.toMutableList())
    }

    override fun getItemCount(): Int {
        return cargoRequests.keys.size
    }

    private fun setupCarrierRequestsView(
        holder: ViewHolder,
        cargoRequests: MutableList<CargoRequest>
    ) {
        holder.carrierRequestsView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RequestsAdapter(
                cargoRequests,
                context,
                fragmentManager,
                fragmentActivity
            )
        }
    }
}