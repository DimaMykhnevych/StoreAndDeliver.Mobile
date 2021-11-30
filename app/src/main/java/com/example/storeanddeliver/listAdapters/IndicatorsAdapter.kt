package com.example.storeanddeliver.listAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.models.UserCargoSnapshots

class IndicatorsAdapter(
    private val cargoIndicators: MutableList<UserCargoSnapshots>,
    private val context: Context
) :
    RecyclerView.Adapter<IndicatorsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cargoIndicatorsHeader: TextView = itemView.findViewById(R.id.cargo_indicators_header)
        val initialSettingsView: RecyclerView = itemView.findViewById(R.id.initialSettingsView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cargo_indicators_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCargo = cargoIndicators[position].cargo
        holder.cargoIndicatorsHeader.text =
            context.getString(R.string.cargo_indicators_header, currentCargo?.description)
        setupCargoSnapshotsRecyclerView(holder, position)
    }

    override fun getItemCount(): Int {
        return cargoIndicators.size
    }

    private fun setupCargoSnapshotsRecyclerView(holder: ViewHolder, position: Int) {
        holder.initialSettingsView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CargoSettingsAdapter(
                cargoIndicators[position].cargo?.cargoSettings?.toMutableList(),
                context
            )
        }
    }
}