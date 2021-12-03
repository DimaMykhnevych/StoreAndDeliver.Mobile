package com.example.storeanddeliver.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.storeanddeliver.R

class AddSessionNoteDialog(onNoteAdded: (String) -> Unit) : DialogFragment() {
    private var callback: (String) -> Unit = onNoteAdded
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.add_session_note_dialog, null)
            val etAddNote = view.findViewById<EditText>(R.id.et_add_note)
            builder.setView(view)
                .setPositiveButton(
                    R.string.add_note
                ) { _, _ ->
                    if(etAddNote.text.isNotEmpty()){
                        callback(etAddNote.text.toString())
                    }
                }
                .setNegativeButton(
                    R.string.cancel
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}