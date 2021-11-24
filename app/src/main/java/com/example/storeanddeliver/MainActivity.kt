package com.example.storeanddeliver

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.storeanddeliver.models.AuthResponseModel
import com.example.storeanddeliver.models.LoginModel
import com.example.storeanddeliver.services.AuthService
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity(), Callback {
    private var tvTest: TextView? = null
    private var loginResponse: AuthResponseModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var authService = AuthService()

        authService.authorize(LoginModel("Dima", "Aa_12345"), this)
    }

    override fun onFailure(call: Call, e: IOException) {
        TODO("Not yet implemented")
    }

    override fun onResponse(call: Call, response: Response) {
        var text = response.body!!.string()
        parseResponse(text)
        updateView(text)
    }

    private  fun parseResponse(response: String){
        val json = JSONObject(response)
        loginResponse = Json.decodeFromString(AuthResponseModel.serializer(), response)
    }

    private fun updateView(text: String){
        tvTest = findViewById(R.id.test)
        runOnUiThread { tvTest?.text = loginResponse?.userInfo?.userName }
    }
}