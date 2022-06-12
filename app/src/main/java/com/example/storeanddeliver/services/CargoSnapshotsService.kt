package com.example.storeanddeliver.services

import com.example.storeanddeliver.constants.Constants
import com.example.storeanddeliver.managers.CredentialsManager
import com.example.storeanddeliver.models.GetCargoSnapshotModel
import okhttp3.*
import java.io.IOException
import android.net.Uri.Builder

class CargoSnapshotsService : BaseHttpService() {
    fun getUserCargoSnapshots(
        getCargoSnapshotModel: GetCargoSnapshotModel,
        callback: (Call, Response) -> Unit
    ) {
        val builder = Builder()
        builder.scheme(Constants.scheme)
            .encodedAuthority(Constants.authority)
            .appendPath(Constants.base)
            .appendPath("CargoSnapshot")
            .appendPath("getUserCargoSnapshots")
            .appendQueryParameter("Temperature", getCargoSnapshotModel.Temperature.toString())
            .appendQueryParameter("Humidity", getCargoSnapshotModel.Humidity.toString())
            .appendQueryParameter("Luminosity", getCargoSnapshotModel.Luminosity.toString())
        val url = builder.build().toString()
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