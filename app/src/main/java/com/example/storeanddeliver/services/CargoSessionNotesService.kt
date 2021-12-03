package com.example.storeanddeliver.services

import com.example.storeanddeliver.constants.Constants
import com.example.storeanddeliver.managers.CredentialsManager
import com.example.storeanddeliver.models.AddCargoSessionNoteModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
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

    fun addCargoSessionNote(note: AddCargoSessionNoteModel, callback: (Call, Response) -> Unit) {
        val json = Json.encodeToString(note)
        val body: RequestBody = json.toRequestBody(JSON)
        var token: String = CredentialsManager.token
        val request: Request = Request.Builder()
            .addHeader("Authorization", "Bearer $token")
            .url(Constants.baseApiUrl + "/cargoSessionNote/addNoteToCargoRequestInSession")
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