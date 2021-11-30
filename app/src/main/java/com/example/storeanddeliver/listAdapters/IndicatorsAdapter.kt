package com.example.storeanddeliver.listAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.models.AverageCargoSnapshots
import com.example.storeanddeliver.models.CargoSnapshot
import com.example.storeanddeliver.models.UserCargoSnapshots

class IndicatorsAdapter(
    private val cargoIndicators: MutableList<UserCargoSnapshots>,
    private val context: Context
) :
    RecyclerView.Adapter<IndicatorsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cargoIndicatorsHeader: TextView = itemView.findViewById(R.id.cargo_indicators_header)
        val initialSettingsView: RecyclerView = itemView.findViewById(R.id.initialSettingsView)
        val cargoSnapshotsView: RecyclerView = itemView.findViewById(R.id.cargoSnapshotsView)
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
        setupCargoSettingsRecyclerView(holder, position)
        setupCargoSnapshotsRecyclerView(holder, position)
    }

    override fun getItemCount(): Int {
        return cargoIndicators.size
    }

    private fun setupCargoSnapshotsRecyclerView(holder: ViewHolder, position: Int) {
        holder.cargoSnapshotsView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CargoSnapshotsAdapter(
                getAverageCargoSnapshots(cargoIndicators[position].cargoSnapshots?.toMutableList()),
                context
            )
        }
    }

    private fun getAverageCargoSnapshots(cargoSnapshots: MutableList<CargoSnapshot>?)
            : MutableList<AverageCargoSnapshots> {
        val averageCargoSnapshots = mutableListOf<AverageCargoSnapshots>()
        val groupedCargoSnapshots = cargoSnapshots?.groupBy { k -> k.environmentSetting.name }
        for (cargoSnapshot in groupedCargoSnapshots?.entries!!) {
            var sum: Double = 0.0
            for (snapshot in cargoSnapshot.value) {
                sum += snapshot.value
            }
            averageCargoSnapshots.add(
                AverageCargoSnapshots(
                    cargoSnapshot.key,
                    sum / groupedCargoSnapshots[cargoSnapshot.key]!!.size
                )
            )
        }
        return averageCargoSnapshots
    }

    private fun setupCargoSettingsRecyclerView(holder: ViewHolder, position: Int) {
        holder.initialSettingsView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CargoSettingsAdapter(
                cargoIndicators[position].cargo?.cargoSettings?.toMutableList(),
                context
            )
        }
    }
}