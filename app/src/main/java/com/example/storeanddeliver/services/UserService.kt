package com.example.storeanddeliver.services

import com.example.storeanddeliver.constants.Constants
import okhttp3.Callback
import okhttp3.Request

class UserService : BaseHttpService() {
    fun getCurrentUserInfo(callback: Callback){
        val request: Request = Request.Builder()
            .url(Constants.baseApiUrl + "/auth/token")
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .get()
            .build()
        okHttpClient.newCall(request).enqueue(callback)
    }
}