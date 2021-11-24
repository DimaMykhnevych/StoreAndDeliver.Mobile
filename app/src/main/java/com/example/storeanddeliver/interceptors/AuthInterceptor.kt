package com.example.storeanddeliver.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run{
        var token: String
        proceed(
            request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ")
                .build()
        )
    }

}