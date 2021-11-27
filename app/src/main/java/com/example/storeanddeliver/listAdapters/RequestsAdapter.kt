package com.example.storeanddeliver.listAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.models.CargoRequest

class RequestsAdapter(private val requests: MutableList<CargoRequest>) :
    RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstNam: TextView = itemView.findViewById(R.id.test)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cargo_request_row, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.firstNam.text = requests[position].cargo?.description
    }

    override fun getItemCount(): Int {
        return requests.size
    }
}