package com.trolla.healthsdk.core

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trolla.healthsdk.R
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_orders.data.ModelOrderProductImage
import com.trolla.healthsdk.utils.TrollaHealthUtility

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

        @BindingAdapter("addressType")
        @JvmStatic
        fun setAddressType(view: AppCompatImageView, addressTye: String?) {
            if (addressTye == null) {
                view.setImageResource(R.drawable.ic_location_filled)
            } else if (addressTye.lowercase() == "work") {
                view.setImageResource(R.drawable.ic_work)
            } else if (addressTye.lowercase() == "home") {
                view.setImageResource(R.drawable.ic_home_active)
            } else {
                view.setImageResource(R.drawable.ic_location_filled)
            }
        }

        @BindingAdapter("setAddressText")
        @JvmStatic
        fun setAddressText(view: TextView, modelAddress: ModelAddress?) {

            modelAddress?.let {
                view.text =
                    modelAddress.name + "\n" + modelAddress.address + " " + modelAddress.landmark + " " + modelAddress.city + " " + modelAddress.state + "\n" + modelAddress.pincode
            }

        }


        @BindingAdapter("capitaliseString")
        @JvmStatic
        fun capitaliseString(view: TextView, addressTye: String?) {
            if (addressTye == null) {
                view.text = "Home"
            } else {
                view.text = addressTye.replaceFirstChar { it.uppercase() }
            }
        }

        @BindingAdapter("setOrderStatusIcon")
        @JvmStatic
        fun setOrderStatusIcon(view: AppCompatImageView, orderStatus: String?) {
            orderStatus?.let {
                if (orderStatus.lowercase() == "pending") {
                    view.setImageResource(R.drawable.ic_order_status_inprocess)
                } else if (orderStatus.lowercase() == "cancelled") {
                    view.setImageResource(R.drawable.ic_order_status_cancelled)
                } else if (orderStatus.lowercase() == "delivered") {
                    view.setImageResource(R.drawable.ic_order_status_delivered)
                } else {
                    view.setImageResource(R.drawable.ic_order_status_inprocess)
                }
            }
        }

        @BindingAdapter("setOrderPaymentModeIcon")
        @JvmStatic
        fun setOrderPaymentModeIcon(view: AppCompatImageView, paymentMode: String?) {
            paymentMode?.let {
                if (paymentMode.lowercase() == "cod") {
                    view.setImageResource(R.drawable.ic_cod)
                } else if (paymentMode.lowercase() == "prepaid") {
                    view.setImageResource(R.drawable.ic_payonline)
                }
            }
        }

        @BindingAdapter("orderStatus", "orderDate")
        @JvmStatic
        fun setOrderDateTime(view: TextView, orderStatus: String?, orderDate: String) {

            view.text = ": " + TrollaHealthUtility.getDate(orderDate)

            if (orderStatus != null) {
                if (orderStatus.lowercase() == "pending") {
                    view.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.colorOrderInProgress
                        )
                    )
                } else if (orderStatus.lowercase() == "cancelled") {
                    view.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.colorOrderCancelled
                        )
                    )
                } else if (orderStatus.lowercase() == "delivered") {
                    view.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.colorOrderDelivered
                        )
                    )
                } else {
                    view.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.colorOrderInProgress
                        )
                    )
                }
            }
        }

        @BindingAdapter("setOrderListProductImages")
        @JvmStatic
        fun setOrderListProductImages(
            rv: RecyclerView?,
            products: ArrayList<GetCartDetailsResponse.CartProduct>
        ) {
            var imagesArray = ArrayList<ModelOrderProductImage>()
            if (products != null) {

                for (i in products.indices) {
                    if (products[i].product.product_img != null && products[i].product.product_img.size > 0) {
                        imagesArray.add(ModelOrderProductImage(products[i].product.product_img[0]))
                    }
                }
                var orderProductImagesAdapter = GenericAdapter(
                    R.layout.item_order_productimage,
                    imagesArray
                )
                rv?.adapter = orderProductImagesAdapter
            }
        }
    }
}