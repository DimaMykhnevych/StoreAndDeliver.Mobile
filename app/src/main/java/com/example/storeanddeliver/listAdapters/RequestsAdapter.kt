package com.example.storeanddeliver.listAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.enums.RequestType
import com.example.storeanddeliver.models.CargoRequest
import java.text.SimpleDateFormat
import java.util.*

class RequestsAdapter(
    private val cargoRequests: MutableList<CargoRequest>,
    private val context: Context
) :
    RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val requestDate: TextView = itemView.findViewById(R.id.request_date)
        val deliverCargoByDate: TextView = itemView.findViewById(R.id.deliver_cargo_by)
        val storeFrom: TextView = itemView.findViewById(R.id.store_cargo_from_date)
        val storeTo: TextView = itemView.findViewById(R.id.store_cargo_to_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cargo_request_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRequest = cargoRequests[position].request
        holder.requestDate.text =
            getDisplayedDateText(R.string.request_from, currentRequest?.requestDate)
        if (currentRequest?.type == RequestType.Deliver.value) {
            holder.deliverCargoByDate.text =
                getDisplayedDateText(R.string.deliver_by, currentRequest.carryOutBefore)
        } else {
            holder.storeFrom.text =
                getDisplayedDateText(R.string.store_cargo_from, currentRequest?.storeFromDate)
            holder.storeTo.text =
                getDisplayedDateText(R.string.store_cargo_to, currentRequest?.storeUntilDate)
        }
    }

    override fun getItemCount(): Int {
        return cargoRequests.size
    }

    private fun getDisplayedDateText(resourceStringId: Int, date: String?): String {
        return context.getString(
            resourceStringId,
            getDateInNeededFormat(date)
        )
    }

    private fun getDateInNeededFormat(dateString: String?): String {
        val pattern = "MM/dd/yyyy"
        val requestDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            .parse(dateString ?: "")
        return SimpleDateFormat(pattern, Locale.ENGLISH).format(requestDate ?: "")
    }
}