package com.example.storeanddeliver.listAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.models.CargoSessionNote
import java.text.SimpleDateFormat
import java.util.*

class SessionNotesAdapter(
    private val sessionNotes: MutableList<CargoSessionNote>
) :
    RecyclerView.Adapter<SessionNotesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNoteTime: TextView = itemView.findViewById(R.id.note_time)
        val tvNoteText: TextView = itemView.findViewById(R.id.note_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.session_notes_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNote = sessionNotes[position]
        holder.tvNoteTime.text = getDateInNeededFormat(currentNote.noteCreationDate)
        holder.tvNoteText.text = currentNote.content
    }

    override fun getItemCount(): Int {
        return sessionNotes.size
    }

    private fun getDateInNeededFormat(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
//        cal.add(Calendar.HOUR, -2)
        val dateInNeededFormat = cal.time

        var pattern = when (UserSettingsManager.currentLanguage) {
            "en" -> "MM/dd/yyyy HH:mm"
            else -> "dd/MM/yyyy HH:mm"
        }
        return SimpleDateFormat(pattern, Locale(UserSettingsManager.currentLocale)).format(
            dateInNeededFormat
        )
    }
}