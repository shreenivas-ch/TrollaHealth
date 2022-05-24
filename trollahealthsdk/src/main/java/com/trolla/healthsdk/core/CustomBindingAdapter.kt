package com.trolla.healthsdk.core

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trolla.healthsdk.R

class CustomBindingAdapter {

    companion object {

        val requestOptions = RequestOptions().placeholder(R.drawable.placeholderimage)
            .error(R.drawable.placeholderimage)
        private var isShowPlaceHolder: Boolean = false

        @BindingAdapter("loadImage")
        @JvmStatic
        fun loadImage(view: ImageView, url: String?) {
            if (url != null) {
                if (isShowPlaceHolder) {
                    if (url.isNotEmpty()) {
                        Glide.with(view.context)
                            .load(url)
                            .placeholder(R.drawable.placeholderimage)
                            .into(view)
                    } else {
                        Glide.with(view.context)
                            .load(R.drawable.placeholderimage)
                            .placeholder(R.drawable.placeholderimage)
                            .circleCrop()
                            .into(
                                view
                            )
                    }
                } else {
                    if (url.isNotEmpty()) {
                        Glide.with(view.context)
                            .load(url)
                            .into(view)
                    }
                }
            }
        }

        @BindingAdapter("isShowPlaceHolder")
        @JvmStatic
        fun isShowPlaceHolder(view: ImageView, isShow: Boolean) {
            this.isShowPlaceHolder = isShow
        }
    }
}