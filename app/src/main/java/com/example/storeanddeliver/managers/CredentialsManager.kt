package com.example.storeanddeliver.managers

import com.example.storeanddeliver.models.AuthResponseModel
import com.example.storeanddeliver.models.UserInfo
import java.text.SimpleDateFormat
import java.util.*

object CredentialsManager {
    var userId: String = ""
    var userName: String = ""
    var role: String = ""
    var registryDate: Date = Date()
    var email: String = ""
    var token: String = ""

    fun saveCredentials(authResponse: AuthResponseModel?) {
        authResponse?.userInfo?.let {
            userId = it.userId
            userName = it.userName
            role = it.role
            registryDate = SimpleDateFormat("dd-MM-yyyy").parse(it.registryDate)
            email = it.email
        }
        authResponse?.token?.let { token = it }
    }
}