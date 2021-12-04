package com.example.storeanddeliver.listAdapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.MapsActivity
import com.example.storeanddeliver.R
import com.example.storeanddeliver.constants.Roles
import com.example.storeanddeliver.dialogs.AddSessionNoteDialog
import com.example.storeanddeliver.dialogs.SessionNotesDialog
import com.example.storeanddeliver.enums.LengthUnit
import com.example.storeanddeliver.enums.RequestStatus
import com.example.storeanddeliver.enums.RequestType
import com.example.storeanddeliver.enums.WeightUnit
import com.example.storeanddeliver.managers.CredentialsManager
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.models.AddCargoSessionNoteModel
import com.example.storeanddeliver.models.Address
import com.example.storeanddeliver.models.CargoRequest
import com.example.storeanddeliver.models.CargoSessionNote
import com.example.storeanddeliver.services.CargoSessionNotesService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.snackbar.Snackbar
import okhttp3.Call
import okhttp3.Response
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class RequestsAdapter(
    private val cargoRequests: MutableList<CargoRequest>,
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    private val onMapClickCallback: ((CargoRequest) -> Unit)?
) :
    RecyclerView.Adapter<RequestsAdapter.ViewHolder>() {

    private val cargoSessionNotesService = CargoSessionNotesService()
    private var addNoteCargoRequestId: String = ""

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
        val weight: TextView = itemView.findViewById(R.id.weight)
        val length: TextView = itemView.findViewById(R.id.length)
        val height: TextView = itemView.findViewById(R.id.height)
        val width: TextView = itemView.findViewById(R.id.width)
        val btnShowCargoInfo: Button = itemView.findViewById(R.id.show_cargo_info)
        val btnShowCargoNotes: Button = itemView.findViewById(R.id.show_cargo_notes)
        val btnAddCargoNote: Button = itemView.findViewById(R.id.add_cargo_note)
        val cargoInfoBlock: LinearLayout = itemView.findViewById(R.id.cargo_info_block)
        val storeAddress: TextView = itemView.findViewById(R.id.store_address)
        val securityMode: TextView = itemView.findViewById(R.id.security_mode)
        val cargoStoreInfo: LinearLayout = itemView.findViewById(R.id.cargo_store_info)
        val settingsView: RecyclerView = itemView.findViewById(R.id.settingsView)
        val btnMap: Button = itemView.findViewById(R.id.btn_map)
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
        displayCargoInfo(holder, cargoRequests[position])
        setupSettingsRecyclerView(holder, position)
        // BUTTONS
        holder.btnShowCargoInfo.setOnClickListener { onShowCargoInfoBtnClick(holder) }
        if (cargoRequests[position].status != RequestStatus.Pending.value) {
            holder.btnShowCargoNotes.setOnClickListener {
                onShowCargoNotesBtnClick(
                    holder,
                    cargoRequests[position].id
                )
            }
        } else {
            holder.btnShowCargoNotes.visibility = View.GONE
        }
        if (CredentialsManager.role != Roles.Carrier) {
            holder.btnAddCargoNote.visibility = View.GONE
            holder.btnMap.visibility = View.GONE
        }
        holder.btnAddCargoNote.setOnClickListener {
            onAddCargoNoteBtnClick(cargoRequests[position].id)
        }
        holder.btnMap.setOnClickListener { onBtnMapClick(cargoRequests[position]) }
    }

    override fun getItemCount(): Int {
        return cargoRequests.size
    }

    private fun onBtnMapClick(cargoRequest: CargoRequest) {
        onMapClickCallback?.let { it(cargoRequest) }
    }

    private fun onAddCargoNoteBtnClick(cargoRequestId: String) {
        addNoteCargoRequestId = cargoRequestId
        fragmentActivity.runOnUiThread {
            var dialog = AddSessionNoteDialog(onAddSessionNoteDialogClosed)
            dialog.show(fragmentManager, "add_session_note_dialog")
        }
    }

    private val onAddSessionNoteDialogClosed: (String) -> Unit = {
        val addNoteModel = AddCargoSessionNoteModel(
            id = "5b4997f8-3206-462a-89b5-c990cfacfd67",
            noteCreationDate = "2021-12-03T08:12:00.795Z",
            content = it,
            cargoRequestId = addNoteCargoRequestId
        )
        cargoSessionNotesService.addCargoSessionNote(addNoteModel, onAddCargoNoteResponse)
    }

    private val onAddCargoNoteResponse: (Call, Response) -> Unit = { _, _ ->
        fragmentActivity.runOnUiThread {
            Toast.makeText(context, context.getString(R.string.add_note_success), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun setupSettingsRecyclerView(holder: ViewHolder, position: Int) {
        holder.settingsView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CargoSettingsAdapter(
                cargoRequests[position].cargo?.cargoSettings?.toMutableList(),
                context
            )
        }
    }

    private fun onShowCargoNotesBtnClick(holder: ViewHolder, currentRequestId: String?) {
        cargoSessionNotesService.getNotesByCargoRequestId(currentRequestId!!, onCargoNotesResponse)
    }

    private val onCargoNotesResponse: (Call, Response) -> Unit = { _, response ->
        val responseString = response.body!!.string()
        val kMapper = jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        )
        val typeRef = object : TypeReference<ArrayList<CargoSessionNote>>() {}
        val cargoNotes = kMapper.readValue(responseString, typeRef)
        cargoNotes.sortBy { k -> k.noteCreationDate }
        openNotesDialog(cargoNotes)
    }

    private fun openNotesDialog(notes: MutableList<CargoSessionNote>) {
        fragmentActivity.runOnUiThread {
            var dialog = SessionNotesDialog(notes)
            dialog.show(fragmentManager, "session_notes_dialog")
        }
    }

    private fun onShowCargoInfoBtnClick(holder: ViewHolder) {
        var currentVisibility = holder.cargoInfoBlock.visibility
        if (currentVisibility == View.VISIBLE) {
            holder.cargoInfoBlock.visibility = View.GONE
            holder.btnShowCargoInfo.text = context.getString(R.string.show_cargo_info)
        } else {
            holder.cargoInfoBlock.visibility = View.VISIBLE
            holder.btnShowCargoInfo.text = context.getString(R.string.hide_cargo_info)
        }
    }

    private fun displayCargoInfo(holder: ViewHolder, cargoRequest: CargoRequest) {
        val currentCargo = cargoRequest.cargo
        holder.cargoDescription.text =
            context.getString(R.string.cargo_description, currentCargo?.description)
        holder.requestStatus.text = context.getString(
            R.string.cargo_status,
            getRequestStatusText(cargoRequest.status)
        )
        holder.amount.text = context.getString(
            R.string.cargo_amount,
            currentCargo?.amount
        )
        holder.weight.text = getCargoWeightText(currentCargo?.weight)
        holder.length.text = getCargoDistanceUnitText(
            currentCargo?.length,
            R.string.cargo_length,
        )
        holder.height.text = getCargoDistanceUnitText(
            currentCargo?.height,
            R.string.cargo_height,
        )
        holder.width.text = getCargoDistanceUnitText(
            currentCargo?.width,
            R.string.cargo_width,
        )
        if (cargoRequest.request?.type == RequestType.Store.value) {
            holder.storeAddress.text = context.getString(
                R.string.store_address,
                getStoreAddress(cargoRequest.store?.address)
            )
            holder.securityMode.text =
                getSecurityModeText(cargoRequest.request?.isSecurityModeEnabled)
        } else {
            holder.cargoStoreInfo.visibility = View.GONE
        }
    }

    private fun getCargoDistanceUnitText(value: Double?, stringId: Int): String {
        var postfix = when (UserSettingsManager.units.length) {
            LengthUnit.Meters.value -> context.getString(R.string.metres)
            LengthUnit.Yards.value -> context.getString(R.string.yards)
            else -> context.getString(R.string.metres)
        }
        var unitString = "${"%.2f".format(value)}$postfix";
        return context.getString(stringId, unitString)
    }

    private fun getSecurityModeText(enabled: Boolean?): String {
        var status: String = context.getString(R.string.enabled)
        if (!enabled!!) {
            status = context.getString(R.string.disabled)
        }
        return context.getString(R.string.security_mode, status)
    }


    private fun getCargoWeightText(weight: Double?): String {
        var postfix = when (UserSettingsManager.units.weight) {
            WeightUnit.Kilograms.value -> context.getString(R.string.kg)
            WeightUnit.Pounds.value -> context.getString(R.string.pounds)
            else -> context.getString(R.string.kg)
        }
        var weightString = "${"%.2f".format(weight)}$postfix"
        return context.getString(R.string.cargo_weight, weightString)
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

    private fun getStoreAddress(address: Address?): String {
        return context.getString(
            R.string.address_format,
            address?.country,
            address?.city,
            address?.street
        )
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
        var priceString = "${"%.2f".format(totalSum)}$postfix"
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
        var pattern = when (UserSettingsManager.currentLanguage) {
            "en" -> "MM/dd/yyyy"
            else -> "dd/MM/yyyy"
        }
        val requestDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            .parse(dateString ?: "")
        return SimpleDateFormat(pattern, Locale.ENGLISH).format(requestDate ?: "")
    }
}