package com.example.storeanddeliver.interceptors

import com.example.storeanddeliver.managers.CredentialsManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run{
        var token: String = CredentialsManager.token
        proceed(
            request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        )
    }

}