package com.example.storeanddeliver.listAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.models.Address
import com.example.storeanddeliver.models.Store

class StoresAdapter(
    private val stores: MutableList<Store>,
    private val context: Context,
    private val onStoreDelete: (Store) -> Unit
) : RecyclerView.Adapter<StoresAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storeName: TextView = itemView.findViewById(R.id.store_name)
        val storeVolume: TextView = itemView.findViewById(R.id.store_volume)
        val storeAddress: TextView = itemView.findViewById(R.id.store_address)
        val deleteBtn: Button = itemView.findViewById(R.id.delete_store)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.store_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentStore = stores[position]
        holder.storeName.text = context.getString(R.string.store_name, currentStore.name)
        holder.storeVolume.text =
            context.getString(R.string.store_volume, "%.2f".format(currentStore.maxCargoVolume))
        holder.storeAddress.text = context.getString(
            R.string.store_address,
            getStoreAddress(currentStore.address)
        )
        holder.deleteBtn.setOnClickListener { onStoreDelete(currentStore) }
    }

    override fun getItemCount(): Int {
        return stores.size
    }

    private fun getStoreAddress(address: Address?): String {
        return context.getString(
            R.string.address_format,
            address?.country,
            address?.city,
            address?.street
        )
    }
}