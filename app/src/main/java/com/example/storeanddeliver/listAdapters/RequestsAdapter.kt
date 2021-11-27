package com.example.storeanddeliver.listAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.enums.RequestStatus
import com.example.storeanddeliver.enums.RequestType
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.models.Address
import com.example.storeanddeliver.models.CargoRequest
import org.w3c.dom.Text
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
        val fromAddress: TextView = itemView.findViewById(R.id.from_address)
        val toAddress: TextView = itemView.findViewById(R.id.to_address)
        val toAddressHeader: TextView = itemView.findViewById(R.id.to_address_header)
        val totalPrice: TextView = itemView.findViewById(R.id.total_price)
        val cargoDescription: TextView = itemView.findViewById(R.id.cargo_description)
        val requestStatus: TextView = itemView.findViewById(R.id.status)
        val amount: TextView = itemView.findViewById(R.id.amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cargo_request_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRequest = cargoRequests[position].request
        val fromAddress = cargoRequests[position].request?.fromAddress
        val toAddress = cargoRequests[position].request?.toAddress
        holder.requestDate.text =
            getDisplayedDateText(R.string.request_from, currentRequest?.requestDate)
        if (currentRequest?.type == RequestType.Deliver.value) {
            holder.deliverCargoByDate.text =
                getDisplayedDateText(R.string.deliver_by, currentRequest.carryOutBefore)
            setToAddress(holder, toAddress)
            holder.storeTo.textSize = 0f
        } else {
            holder.storeFrom.text =
                getDisplayedDateText(R.string.store_cargo_from, currentRequest?.storeFromDate)
            holder.storeTo.text =
                getDisplayedDateText(R.string.store_cargo_to, currentRequest?.storeUntilDate)
            holder.toAddressHeader.textSize = 0f
            holder.toAddress.textSize = 0f
        }
        setFromAddressText(holder, fromAddress)
        holder.totalPrice.text = getTotalSumText(currentRequest?.totalSum)

        // CARGO SECTION
        val currentCargo = cargoRequests[position].cargo
        holder.cargoDescription.text =
            context.getString(R.string.cargo_description, currentCargo?.description)
        holder.requestStatus.text = context.getString(
            R.string.cargo_status,
            getRequestStatusText(cargoRequests[position].status)
        )
        holder.amount.text = context.getString(
            R.string.cargo_amount,
            currentCargo?.amount
        )

    }

    override fun getItemCount(): Int {
        return cargoRequests.size
    }

    private fun getRequestStatusText(status: Int): String {
        return when (status) {
            RequestStatus.Pending.value -> context.getString(
                R.string.status_pending
            )
            RequestStatus.InProgress.value -> context.getString(
                R.string.status_active
            )
            RequestStatus.Completed.value -> context.getString(
                R.string.status_completed
            )
            else -> context.getString(
                R.string.status_pending
            )
        }
    }

    private fun setToAddress(holder: ViewHolder, toAddress: Address?) {
        holder.toAddress.text = context.getString(
            R.string.address_format,
            toAddress?.country,
            toAddress?.city,
            toAddress?.street
        )
    }

    private fun setFromAddressText(holder: ViewHolder, fromAddress: Address?) {
        holder.fromAddress.text = context.getString(
            R.string.address_format,
            fromAddress?.country,
            fromAddress?.city,
            fromAddress?.street
        )
    }

    private fun getTotalSumText(totalSum: Double?): String {
        var postfix = when (UserSettingsManager.currentLanguage) {
            "ua" -> "гривень(uah)"
            "ru" -> "рублей(rub)"
            else -> "\$ (usd)"
        }
        var priceString = "$totalSum$postfix"
        return context.getString(
            R.string.total_price,
            priceString
        )
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