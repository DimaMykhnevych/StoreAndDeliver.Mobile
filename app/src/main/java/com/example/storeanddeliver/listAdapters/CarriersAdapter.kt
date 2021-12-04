package com.example.storeanddeliver.listAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.models.Carrier

class CarriersAdapter(
    private val carriers: MutableList<Carrier>,
    private val context: Context,
    private val onCarrierDelete: (Carrier) -> Unit
) : RecyclerView.Adapter<CarriersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val carrierUsername: TextView = itemView.findViewById(R.id.carrier_username)
        val carrierEmail: TextView = itemView.findViewById(R.id.carrier_email)
        val companyName: TextView = itemView.findViewById(R.id.company_name)
        val truckVolume: TextView = itemView.findViewById(R.id.truck_volume)
        val deleteBtn: Button = itemView.findViewById(R.id.delete_carrier)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.carrier_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCarrier = carriers[position]
        holder.carrierUsername.text =
            context.getString(R.string.carrier_username, currentCarrier.appUser.userName)
        holder.carrierEmail.text =
            context.getString(R.string.carrier_email, currentCarrier.appUser.email)
        holder.companyName.text =
            context.getString(R.string.company_name, currentCarrier.companyName)
        holder.truckVolume.text =
            context.getString(R.string.truck_volume, "%.2f".format(currentCarrier.maxCargoVolume))
        holder.deleteBtn.setOnClickListener { onCarrierDelete(currentCarrier) }
    }

    override fun getItemCount(): Int {
        return carriers.size
    }

}