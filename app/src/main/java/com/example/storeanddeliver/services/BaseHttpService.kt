package com.example.storeanddeliver.services

import com.example.storeanddeliver.interceptors.AuthInterceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient

open class BaseHttpService {
    protected val okHttpClient: OkHttpClient = OkHttpClient();
    protected val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()

    init {
        OkHttpClient.Builder().addInterceptor(AuthInterceptor())
    }
}