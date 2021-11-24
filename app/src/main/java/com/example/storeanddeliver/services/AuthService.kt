package com.example.storeanddeliver.services

import com.example.storeanddeliver.constants.Constants
import com.example.storeanddeliver.models.LoginModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Callback
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AuthService : BaseHttpService() {
    fun authorize(loginModel: LoginModel, callback: Callback) {
        val json = Json.encodeToString(loginModel)
        val body: RequestBody = json.toRequestBody(JSON)

        val request: Request = Request.Builder()
            .url(Constants.baseApiUrl + "/auth/token")
            .post(body)
            .build()

        okHttpClient.newCall(request).enqueue(callback)
    }
}