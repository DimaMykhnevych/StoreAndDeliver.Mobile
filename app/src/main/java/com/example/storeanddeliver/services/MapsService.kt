package com.example.storeanddeliver.services

import okhttp3.Callback
import okhttp3.Request

class MapsService : BaseHttpService() {
    fun getRoute(url: String, callback: Callback){
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .get()
            .build()
        okHttpClient.newCall(request).enqueue(callback)
    }
}