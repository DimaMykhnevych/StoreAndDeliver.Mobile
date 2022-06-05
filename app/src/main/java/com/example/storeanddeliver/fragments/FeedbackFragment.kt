package com.example.storeanddeliver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.storeanddeliver.constants.Roles
import com.example.storeanddeliver.databinding.FragmentFeedbackBinding
import com.example.storeanddeliver.managers.CredentialsManager

import android.widget.EditText
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storeanddeliver.R
import com.example.storeanddeliver.listAdapters.FeedbackAdapter
import com.example.storeanddeliver.models.AddFeedbackModel
import com.example.storeanddeliver.models.Feedback
import com.example.storeanddeliver.services.FeedbackService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Call
import okhttp3.Response


class FeedbackFragment : Fragment() {
    private val defaultRating = 2.5F
    private var feedbackList:MutableList<Feedback> = mutableListOf()
    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    private var feedbackService = FeedbackService()
    private lateinit var feedbackView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFeedback()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        initView()
        feedbackView = binding.feedbackView
        binding.progressBarFeedback.isVisible = true
        binding.emptyFeedbackText.visibility = View.GONE
        binding.sendBtn.setOnClickListener { onSendBtnClickListener() }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onSendBtnClickListener() {
        if (!validateTitle()) {
            return
        }
        val addFeedback = AddFeedbackModel(binding.etFeedback.text.toString(), binding.rBar.rating)
        binding.etFeedback.setText("")
        binding.rBar.rating = defaultRating
        feedbackService.addFeedback(addFeedback, onAddFeedbackResponse)
    }

    private val onAddFeedbackResponse: (Call, Response) -> Unit = { _, _ ->

        activity?.runOnUiThread {
            feedbackList.clear()
            setupFeedbackRecyclerView()
            binding.progressBarFeedback.isVisible = true
        }
        getFeedback()
    }

    private val onGetFeedbackResponse: (Call, Response) -> Unit = { _, response ->
        val responseString = response.body!!.string()
        parseGetFeedbackResponse(responseString)
        updateView()
    }

    private fun setupFeedbackRecyclerView() {
        feedbackView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = FeedbackAdapter(feedbackList)
        }
    }

    private fun updateView() {
        activity?.runOnUiThread {
            binding.progressBarFeedback.isVisible = false
            setupFeedbackRecyclerView()
            if (feedbackList.size == 0 && CredentialsManager.role == Roles.CompanyAdmin) {
                binding.emptyFeedbackText.visibility = View.VISIBLE
            } else {
                binding.emptyFeedbackText.visibility = View.GONE
            }
        }
    }

    private fun parseGetFeedbackResponse(response: String) {
        val kMapper = jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false
        )
        val typeRef = object : TypeReference<ArrayList<Feedback>>() {}
        feedbackList = kMapper.readValue(response, typeRef)
    }

    private fun validateTitle(): Boolean {
        val feedback: EditText = binding.etFeedback
        if (feedback.text.toString().trim().isEmpty()) {
            feedback.error = getString(R.string.feedback_validation_message)
            return false
        }
        return true
    }

    private fun initView() {
        when (CredentialsManager.role) {
            Roles.User -> {
                binding.feedbackHeaderAdmin.visibility = View.GONE
            }
            Roles.CompanyAdmin -> {
                binding.feedbackHeaderUser.visibility = View.GONE
                binding.addFeedbackForm.visibility = View.GONE
            }
        }
        binding.rBar.rating = defaultRating
    }

    private fun getFeedback(){
        when (CredentialsManager.role){
            Roles.User -> {
                feedbackService.getUserFeedback(onGetFeedbackResponse)
            }
            Roles.CompanyAdmin -> {
                feedbackService.getFeedbackWithUser(onGetFeedbackResponse)
            }
        }
    }
}