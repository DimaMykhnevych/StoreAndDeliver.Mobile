package com.example.storeanddeliver.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.storeanddeliver.R

class ConfirmDeletionDialog(
    private val deletedItem: String,
    private val deletedItemId: String,
    private val onDelete: (String) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.confirm_deletion, deletedItem))
                .setPositiveButton(
                    R.string.delete
                ) { _, _ ->
                    onDelete(deletedItemId)
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}