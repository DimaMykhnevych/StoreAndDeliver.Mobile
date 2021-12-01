package com.example.storeanddeliver.services

import com.example.storeanddeliver.constants.Constants
import com.example.storeanddeliver.managers.CredentialsManager
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class CargoSessionNotesService : BaseHttpService() {
    fun getNotesByCargoRequestId(cargoRequestId: String,  callback: (Call, Response) -> Unit){
        val url = "${Constants.baseApiUrl}/cargoSessionNote/getNotesByCargoRequestId/$cargoRequestId"
        var token: String = CredentialsManager.token
        val request: Request = Request.Builder()
            .addHeader("Authorization", "Bearer $token")
            .url(url)
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
}