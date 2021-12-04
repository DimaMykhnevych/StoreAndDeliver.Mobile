package com.example.storeanddeliver.services

import com.example.storeanddeliver.constants.Constants
import com.example.storeanddeliver.managers.CredentialsManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class CarrierService : BaseHttpService() {
    fun getCarriers(callback: (Call, Response) -> Unit) {
        val token: String = CredentialsManager.token
        val request: Request = Request.Builder()
            .url(Constants.baseApiUrl + "/carrier")
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

    fun deleteCarrier(id: String, callback: (Call, Response) -> Unit) {
        val token: String = CredentialsManager.token
        val request: Request = Request.Builder()
            .url(Constants.baseApiUrl + "/carrier/$id")
            .addHeader("Authorization", "Bearer $token")
            .delete()
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