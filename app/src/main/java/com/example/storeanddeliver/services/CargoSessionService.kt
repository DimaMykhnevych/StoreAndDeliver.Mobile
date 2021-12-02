package com.example.storeanddeliver.services

import com.example.storeanddeliver.constants.Constants
import com.example.storeanddeliver.managers.CredentialsManager
import com.example.storeanddeliver.models.GetOptimizedRequestModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class CargoSessionService : BaseHttpService() {
    fun getCarrierRequests(
        getRequestsModel: GetOptimizedRequestModel,
        callback: (Call, Response) -> Unit
    ) {
        val json = Json.encodeToString(getRequestsModel)
        val body: RequestBody = json.toRequestBody(JSON)
        var token: String = CredentialsManager.token
        val request: Request = Request.Builder()
            .addHeader("Authorization", "Bearer $token")
            .url(Constants.baseApiUrl + "/cargoSession/filteredCarrierRequests")
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