package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.utils.Constant.ResultKeys.KEY_CONFIDENCE
import com.dicoding.asclepius.utils.Constant.ResultKeys.KEY_IMAGE_URI
import com.dicoding.asclepius.utils.Constant.ResultKeys.KEY_RESULT

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getParcelableExtra<Uri>(KEY_IMAGE_URI)
        val result = intent.getStringExtra(KEY_RESULT)
        val confidence = intent.getFloatExtra(KEY_CONFIDENCE, 0f)

        binding.resultImage.setImageURI(imageUri)
        binding.resultText.text = getString(R.string.classification_result, result, confidence)
    }
}