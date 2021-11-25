package com.example.storeanddeliver

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.storeanddeliver.managers.CredentialsManager
import com.example.storeanddeliver.models.AuthResponseModel
import com.example.storeanddeliver.models.LoginModel
import com.example.storeanddeliver.services.AuthService
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.storeanddeliver.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import android.view.Gravity
import android.view.View

import android.widget.FrameLayout
import android.widget.Toast
import com.example.storeanddeliver.enums.LoginErrorCode


class MainActivity : AppCompatActivity(), Callback {
    private var loginResponse: AuthResponseModel? = null
    private lateinit var mBinding: ActivityMainBinding
    private var errorText: String = ""
    private val userName = MutableStateFlow("")
    private val password = MutableStateFlow("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        with(mBinding) {

            etUserName.doOnTextChanged { text, _, _, _ ->
                userName.value = text.toString()
            }
            etPassword.doOnTextChanged { text, _, _, _ ->
                password.value = text.toString()
            }
        }

        mBinding.btnLogin.setOnClickListener { onLoginButtonClick() }

        lifecycleScope.launch {
            formIsValid.collect {
                mBinding.btnLogin.apply {
                    isClickable = it
                    isEnabled = it
                }
            }
        }
    }

    private val formIsValid = combine(
        userName,
        password,
    ) { userName, password ->
        mBinding.txtErrorMessage.text = ""
        val usernameIsValid = userName.length > 1
        val passwordIsValid = password.length > 6
        usernameIsValid and passwordIsValid
    }

    override fun onFailure(call: Call, e: IOException) {
        mBinding.progressBarCyclic.visibility = View.GONE
        e.printStackTrace()
    }

    override fun onResponse(call: Call, response: Response) {
        var text = response.body!!.string()
        parseResponse(text)
        updateView(text)
    }

    private fun parseResponse(response: String) {
        loginResponse = Json.decodeFromString(AuthResponseModel.serializer(), response)
        CredentialsManager.saveCredentials(loginResponse)
    }

    private fun updateView(text: String) {
        runOnUiThread {
            mBinding.progressBarCyclic.visibility = View.GONE
            mBinding.btnLogin.isEnabled = true
            if (loginResponse!!.isAuthorized) {
                mBinding.txtErrorMessage.text = ""
                Snackbar
                    .make(mBinding.root, getString(R.string.success_login), Snackbar.LENGTH_LONG)
                    .show()
                navigateToNextScreen()
            } else {
                onLoginError()
            }
        }
    }

    private fun navigateToNextScreen(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun onLoginError() {
        loginResponse?.let {
            errorText = when(LoginErrorCode.fromInt(it.loginErrorCode)){
                LoginErrorCode.InvalidUsernameOrPassword -> {
                    getString(R.string.invalid_creds)
                }
                LoginErrorCode.EmailConfirmationRequired -> {
                    getString(R.string.email_confirmation_required)
                }
                else -> ""
            }
            mBinding.txtErrorMessage.text = errorText
        }
    }

    private fun onLoginButtonClick() {
        var authService = AuthService()
        mBinding.progressBarCyclic.visibility = View.VISIBLE
        mBinding.btnLogin.isEnabled = false
        authService.authorize(
            LoginModel(
                mBinding.etUserName.text.toString(),
                mBinding.etPassword.text.toString()
            ), this
        )
    }
}