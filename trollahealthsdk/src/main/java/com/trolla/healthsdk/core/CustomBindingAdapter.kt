package com.trolla.healthsdk.core

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trolla.healthsdk.R
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.utils.LogUtil

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

        @BindingAdapter("setOfferText")
        @JvmStatic
        fun setOfferText(view: TextView, dashboardProduct: DashboardProduct?) {
            if (dashboardProduct != null) {
                if (dashboardProduct.discount != "0" || dashboardProduct.discount != "") {
                    val discountString =
                        when (dashboardProduct.discount_type) {
                            "1" -> {
                                "Rs. " + dashboardProduct.discount + " OFF"
                            }
                            "2" -> {
                                dashboardProduct.discount + "% OFF"
                            }
                            else -> {
                                ""
                            }
                        }


                    LogUtil.printObject("----->$discountString")

                    if (discountString != "") {
                        view.visibility = View.VISIBLE
                        view.text = discountString
                    } else {
                        view.visibility = View.GONE
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