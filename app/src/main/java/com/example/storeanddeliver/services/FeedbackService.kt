package com.example.storeanddeliver.services

import com.example.storeanddeliver.constants.Constants
import com.example.storeanddeliver.managers.CredentialsManager
import com.example.storeanddeliver.models.AddFeedbackModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class FeedbackService : BaseHttpService() {
    fun getFeedbackWithUser(callback: (Call, Response) -> Unit) {
        val token: String = CredentialsManager.token
        val request: Request = Request.Builder()
            .url(Constants.baseApiUrl + "/feedback/getFeedbackWithUser")
            .addHeader("Authorization", "Bearer $token")
            .get()
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                callback(call, response)
            }
        })
    }

    fun getUserFeedback(callback: (Call, Response) -> Unit) {
        val token: String = CredentialsManager.token
        val request: Request = Request.Builder()
            .url(Constants.baseApiUrl + "/feedback/getUserFeedback")
            .addHeader("Authorization", "Bearer $token")
            .get()
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                callback(call, response)
            }
        })
    }

    fun addFeedback(feedback: AddFeedbackModel, callback: (Call, Response) -> Unit) {
        val json = Json.encodeToString(feedback)
        val body: RequestBody = json.toRequestBody(JSON)
        var token: String = CredentialsManager.token
        val request: Request = Request.Builder()
            .addHeader("Authorization", "Bearer $token")
            .url(Constants.baseApiUrl + "/feedback")
            .post(body)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                callback(call, response)
            }

        })
    }
}