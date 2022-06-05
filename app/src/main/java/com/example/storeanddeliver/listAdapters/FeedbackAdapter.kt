package com.example.storeanddeliver.listAdapters

import android.annotation.SuppressLint
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.models.Feedback
import java.text.SimpleDateFormat
import java.util.*
import android.view.MotionEvent
import android.view.View.OnTouchListener


class FeedbackAdapter(
    private val feedback: MutableList<Feedback>
) :
    RecyclerView.Adapter<FeedbackAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTv: TextView = itemView.findViewById(R.id.username_tv)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val emailTv: TextView = itemView.findViewById(R.id.email_tv)
        val dateTv: TextView = itemView.findViewById(R.id.date_tv)
        val contentTv: TextView = itemView.findViewById(R.id.content_tv)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.feedback_row, parent, false)
        val contentView = view.findViewById<TextView>(R.id.content_tv)
        contentView.movementMethod = ScrollingMovementMethod()
        contentView.setOnTouchListener(OnTouchListener { v, _ ->
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        })
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFeedback = feedback[position]

        holder.contentTv.text = currentFeedback.content
        holder.usernameTv.text = currentFeedback.username
        holder.emailTv.text = currentFeedback.userEmail
        holder.ratingBar.rating = currentFeedback.rating
        holder.dateTv.text = getDateInNeededFormat(currentFeedback.date)
    }

    override fun getItemCount(): Int {
        return feedback.size
    }

    private fun getDateInNeededFormat(dateString: String?): String {
        var pattern = when (UserSettingsManager.currentLanguage) {
            "en" -> "MM/dd/yyyy HH:mm"
            else -> "dd/MM/yyyy HH:mm"
        }
        val feedbackDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
            .parse(dateString ?: "")
        return SimpleDateFormat(pattern, Locale.ENGLISH).format(feedbackDate ?: "")
    }
}