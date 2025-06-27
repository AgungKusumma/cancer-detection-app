package com.dicoding.asclepius.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private val requiredPermission: String
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                showToast(getString(R.string.permission_granted))
                startGallery()
            } else {
                showToast(getString(R.string.permission_denied))
            }
        }

    private val launcherGalleryIntent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri? = result.data?.data
            selectedImg?.let {
                currentImageUri = it
                showImage()
            }
        }
    }

    private val launcherPhotoPicker = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            showImage()
        } ?: showToast(getString(R.string.no_image_selected))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted() && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requestPermission()
        }

        setupAction()
    }

    private fun setupAction() = with(binding) {
        galleryButton.setOnClickListener { handleGalleryAccess() }
    }

    private fun handleGalleryAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+, no need for permission (Photo Picker handles it)
            startGallery()
        } else {
            // < Android 13, need to check/request storage permission first
            if (allPermissionsGranted()) {
                startGallery()
            } else {
                if (shouldShowRequestPermissionRationale(requiredPermission)) {
                    // User rejected but still can be asked again
                    showToast(getString(R.string.permission_required))
                    requestPermission()
                } else {
                    // Permission permanently denied (2x reject or manual disable)
                    showToast(getString(R.string.permission_permanently_denied))
                    openAppSettings()
                }
            }
        }
    }

    private fun startGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcherPhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            launcherGalleryIntent.launch(intent)
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun allPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            requiredPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(requiredPermission)
    }

    private fun openAppSettings() {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = "package:$packageName".toUri()
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
