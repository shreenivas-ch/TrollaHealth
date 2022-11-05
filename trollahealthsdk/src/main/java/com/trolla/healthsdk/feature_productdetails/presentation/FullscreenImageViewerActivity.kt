package com.trolla.healthsdk.feature_productdetails.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.trolla.healthsdk.R

class FullscreenImageViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image_viewer)

        findViewById<AppCompatImageView>(R.id.imgBack).setOnClickListener {
            finish()
        }

        var imageUrl = intent.getStringExtra("imageurl")

        var photoview = findViewById<PhotoView>(R.id.photo_view)
        Glide.with(this).load(imageUrl).into(photoview)

    }
}