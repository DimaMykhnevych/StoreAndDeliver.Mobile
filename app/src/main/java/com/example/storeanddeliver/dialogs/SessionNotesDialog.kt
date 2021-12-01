package com.example.storeanddeliver.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.listAdapters.SessionNotesAdapter
import com.example.storeanddeliver.models.CargoSessionNote

class SessionNotesDialog(notes: MutableList<CargoSessionNote>) : DialogFragment() {
    private lateinit var notesView: RecyclerView
    private var notes = notes
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.session_notes_dialog, null)
            notesView = view.findViewById(R.id.notesView)
            var tvEmptyNotes: TextView = view.findViewById(R.id.tv_empty_notes)
            if(notes.size == 0){
                tvEmptyNotes.visibility = View.VISIBLE
            } else{
                tvEmptyNotes.visibility = View.GONE
            }
            setupNoteRecyclerView()
            builder.setView(view)
                .setNegativeButton(
                    R.string.close
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setupNoteRecyclerView() {
        notesView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = SessionNotesAdapter(notes)
        }
    }
}