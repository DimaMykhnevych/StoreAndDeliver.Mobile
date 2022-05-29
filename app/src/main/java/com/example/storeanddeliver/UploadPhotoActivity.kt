package com.example.storeanddeliver;

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore
import com.example.storeanddeliver.constants.Constants
import com.example.storeanddeliver.databinding.ActivityUploadPhotoBinding
import com.example.storeanddeliver.services.CargoRequestService
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import android.app.Activity


class UploadPhotoActivity : AppCompatActivity() {
    private lateinit var cargoRequestId: String
    private lateinit var binding: ActivityUploadPhotoBinding;
    private val selectImageRequestCode: Int = 100;
    private var cargoRequestService = CargoRequestService()
    private var imagePath: Uri? = null
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cargoRequestId = intent.getStringExtra(Constants.cargoRequestIdKey)!!
        binding = ActivityUploadPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectImageBtn.setOnClickListener { onSelectImageBtnClick() }
        binding.savePhotoBtn.setOnClickListener { onSavePhotoBtnClick() }
    }

    private fun onSelectImageBtnClick() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, selectImageRequestCode)
    }

    private fun onSavePhotoBtnClick() {
        if (imagePath == null) {
            return
        }

        verifyStoragePermissions(this)
        imagePath?.let { cargoRequestService.uploadFile(it, cargoRequestId, this, onPhotoUploaded) }
    }

    private val onPhotoUploaded: () -> Unit = {
        val intent = Intent(this@UploadPhotoActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == selectImageRequestCode) {
            imagePath = data?.data
            binding.imageViewForm.setImageURI(imagePath)
        }
    }

    private fun verifyStoragePermissions(activity: Activity?) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }
}