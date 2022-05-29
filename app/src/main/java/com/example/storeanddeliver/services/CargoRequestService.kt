package com.example.storeanddeliver.services

import android.app.Activity
import android.app.ProgressDialog
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.example.storeanddeliver.R
import com.example.storeanddeliver.constants.Constants
import com.example.storeanddeliver.helpers.URIPathHelper
import com.example.storeanddeliver.managers.CredentialsManager
import com.example.storeanddeliver.models.GetOptimizedRequestModel
import com.example.storeanddeliver.models.UpdateCargoRequestModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class CargoRequestService : BaseHttpService() {
    var dialog: ProgressDialog? = null

    fun getUserCargoRequests(
        getRequestsModel: GetOptimizedRequestModel,
        callback: (Call, Response) -> Unit
    ) {
        val json = Json.encodeToString(getRequestsModel)
        val body: RequestBody = json.toRequestBody(JSON)
        var token: String = CredentialsManager.token
        val request: Request = Request.Builder()
            .addHeader("Authorization", "Bearer $token")
            .url(Constants.baseApiUrl + "/cargoRequest/getUserCargoRequests")
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

    fun updateRequestStatuses(
        updateModel: UpdateCargoRequestModel,
        callback: (Call, Response) -> Unit
    ) {
        val json = Json.encodeToString(updateModel)
        val body: RequestBody = json.toRequestBody(JSON)
        var token: String = CredentialsManager.token
        val request: Request = Request.Builder()
            .addHeader("Authorization", "Bearer $token")
            .url(Constants.baseApiUrl + "/request/updateRequestStautses")
            .put(body)
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                callback(call, response)
            }

        })
    }

    fun uploadFile(
        sourceFilePath: Uri,
        cargoRequestId: String,
        context: Activity,
        callback: () -> Unit
    ) {
        val pathFromUri = URIPathHelper().getPath(context, sourceFilePath)
        uploadFile(File(pathFromUri!!), cargoRequestId, context, callback)
    }

    private fun uploadFile(
        sourceFile: File,
        cargoRequestId: String,
        context: Activity,
        callback: () -> Unit
    ) {
        Thread {
            val mimeType = getMimeType(sourceFile);
            if (mimeType == null) {
                Log.e("file error", "Not able to get mime type")
                return@Thread
            }
            val fileName: String = generateUniquePhotoName(sourceFile.name)
            toggleProgressDialog(true, context)
            try {
                val requestBody: RequestBody =
                    MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart(
                            "uploaded_file",
                            fileName,
                            sourceFile.asRequestBody(mimeType.toMediaTypeOrNull())
                        )
                        .build()
                val token: String = CredentialsManager.token
                val request: Request = Request.Builder()
                    .addHeader("Authorization", "Bearer $token")
                    .url(Constants.baseApiUrl + "/cargoRequest/uploadCargoPhoto/$cargoRequestId")
                    .post(requestBody)
                    .build()

                val client = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    Log.d("File upload", "success")
                    showToast(context.resources.getString(R.string.upload_success), context)
                } else {
                    Log.e("File upload", "failed")
                    showToast(context.resources.getString(R.string.upload_failed), context)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                Log.e("File upload", "failed")
                showToast(context.resources.getString(R.string.upload_failed), context)
            }
            toggleProgressDialog(false, context)
            callback()
        }.start()
    }

    private fun generateUniquePhotoName(initialName: String): String {
        var splitedFileName = initialName
            .replace(" ", "-")
            .split(".")
            .filter { s -> s != "" }
            .toMutableList()
        val fileFormat =
            splitedFileName[splitedFileName.size - 1]
        splitedFileName.removeLast()
        return splitedFileName.joinToString(separator = ".") + UUID.randomUUID()
            .toString() + "." + fileFormat
    }

    private fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = file.path.split(".");
        if (extension.size > 1) {
            type =
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension[extension.size - 1])
        }
        return type
    }

    private fun showToast(message: String, context: Activity) {
        context.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun toggleProgressDialog(show: Boolean, context: Activity) {
        context.runOnUiThread {
            if (show) {
                dialog = ProgressDialog.show(
                    context,
                    "",
                    context.resources.getString(R.string.uploading_file),
                    true
                );
            } else {
                dialog?.dismiss();
            }
        }
    }
}