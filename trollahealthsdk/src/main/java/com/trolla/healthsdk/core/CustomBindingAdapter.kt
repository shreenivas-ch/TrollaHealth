package com.trolla.healthsdk.core

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trolla.healthsdk.R
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct
import com.trolla.healthsdk.feature_orders.data.ModelOrder
import com.trolla.healthsdk.feature_orders.data.ModelOrderProductImage
import com.trolla.healthsdk.utils.*


class CustomBindingAdapter {

    companion object {

        private var isShowPlaceHolder: Boolean = false

        @BindingAdapter("loadImage")
        @JvmStatic
        fun loadImage(view: ImageView, url: String?) {
            if (url != null) {
                if (isShowPlaceHolder) {
                    if (url.isNotEmpty()) {
                        Glide.with(view.context)
                            .load(url)
                            .placeholder(R.drawable.ic_default_productimage)
                            .into(view)
                    } else {
                        Glide.with(view.context)
                            .load(R.drawable.ic_default_productimage)
                            .placeholder(R.drawable.ic_default_productimage)
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
            } else {
                Glide.with(view.context)
                    .load(R.drawable.ic_default_productimage)
                    .placeholder(R.drawable.ic_default_productimage)
                    .into(
                        view
                    )


            }
        }

        @BindingAdapter("rxType", "product")
        @JvmStatic
        fun setMrpBasedOnRxType(textView: TextView, rxType: String, product: DashboardProduct) {
            if (rxType.lowercase() == "rx") {
                textView.text = String.format(
                    textView.context.getString(R.string.amount_string),
                    product.rx_offer_mrp.toString()
                )
            } else {
                textView.text = String.format(
                    textView.context.getString(R.string.amount_string),
                    product.sale_price
                )
            }
        }

        @BindingAdapter("rxType", "cartProduct")
        @JvmStatic
        fun setMrpBasedOnRxTypeInCart(
            textView: TextView,
            rxType: String,
            cartProduct: GetCartDetailsResponse.CartProduct
        ) {
            if (rxType.lowercase() == "rx") {
                textView.text = String.format(
                    textView.context.getString(R.string.amount_string),
                    cartProduct.product.rx_offer_mrp.toString()
                )
            } else {
                textView.text = String.format(
                    textView.context.getString(R.string.amount_string),
                    cartProduct.product.sale_price
                )
            }
        }

        @BindingAdapter("setOfferText")
        @JvmStatic
        fun setOfferText(view: TextView, dashboardProduct: DashboardProduct?) {
            if (dashboardProduct != null) {

                var discount =
                    if (dashboardProduct.rx_type.lowercase() == "rx") dashboardProduct.rx_offer_desc else dashboardProduct.discount

                if (discount != "0" || discount != "") {
                    val discountString =
                        when (dashboardProduct.discount_type) {
                            "1" -> {
                                "Rs. $discount OFF"
                            }
                            "2" -> {
                                "$discount% OFF"
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

        @BindingAdapter("setDrawableProductVariant")
        @JvmStatic
        fun setDrawableProductVariant(
            view: RelativeLayout,
            productVariantValues: DashboardResponse.ProductVariantValues
        ) {
            if (productVariantValues.product_id.toString() == productVariantValues.currentProducId) {
                view.setBackgroundResource(R.drawable.bg_circular_primarycolor_filled)
            } else {
                view.setBackgroundResource(R.drawable.bg_circular_primarycolor)
            }
        }

        @BindingAdapter("setTextColorProductVariant")
        @JvmStatic
        fun setTextColorProductVariant(
            view: TextView,
            productVariantValues: DashboardResponse.ProductVariantValues
        ) {
            if (productVariantValues.product_id.toString() == productVariantValues.currentProducId) {
                view.setTextColor(ContextCompat.getColor(view.context,R.color.white))
            } else {
                view.setTextColor(ContextCompat.getColor(view.context,R.color.primary_color))
            }
        }

        @BindingAdapter("setStrikeThrough", "saleprice")
        @JvmStatic
        fun setStrikeThrough(strike: TextView, actualprice: String, saleprice: String) {
            if (actualprice == saleprice) {
                strike.invisible()
            } else {
                strike.show()
                strike.paintFlags = strike.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }


        @BindingAdapter("setOrderPlaceDateAndOrderId")
        @JvmStatic
        fun setOrderPlaceDateAndOrderId(view: TextView, modelOrder: ModelOrder) {

            var orderText =
                "Order #" + modelOrder.order_id + " (" + modelOrder.products.size + if (modelOrder.products.size == 1) " Item)" else " Items)"
            if (modelOrder.created_at.isNullOrEmpty() || modelOrder.created_at == "null") {

            } else {
                orderText += "     " + TrollaHealthUtility.convertOrderDate(modelOrder.created_at)
            }
            view.text = orderText
        }

        @BindingAdapter("orderStatus", "orderDate")
        @JvmStatic
        fun setOrderDateTime(view: TextView, orderStatus: String?, orderDate: String?) {

            var pretext =
                if (orderStatus!!.lowercase() == TrollaConstants.ORDERSTATUS_CANCELLED || orderStatus!!.lowercase() == TrollaConstants.ORDERSTATUS_DELIVERED) {
                    ""
                } else {
                    "ETA: "
                }
            if (orderDate.isNullOrEmpty() || orderDate == "null") {
                view.text = pretext + "Not Available"
            } else {
                view.text = pretext + TrollaHealthUtility.getDate(orderDate) ?: "Not Available"
            }

            if (orderStatus != null) {
                if (orderStatus.lowercase() == TrollaConstants.ORDERSTATUS_PENDING) {
                    view.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.colorOrderInProgress
                        )
                    )
                } else if (orderStatus.lowercase() == TrollaConstants.ORDERSTATUS_CANCELLED) {
                    view.setTextColor(
                        ContextCompat.getColor(
                            view.context,
                            R.color.colorOrderCancelled
                        )
                    )
                } else if (orderStatus.lowercase() == TrollaConstants.ORDERSTATUS_DELIVERED) {
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
            /*rv?.removeAllViews()
            val inflater =
                rv?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater*/

            var imagesArray = ArrayList<ModelOrderProductImage>()
            if (products != null) {

                for (i in products.indices) {
                    if (products[i].product.product_img != null && products[i].product.product_img.size > 0) {
                        imagesArray.add(ModelOrderProductImage(products[i].product.product_img[0]))

                        /*val layout2: View = inflater.inflate(R.layout.item_order_productimage, null)
                        var img = layout2.findViewById<AppCompatImageView>(R.id.imvBanner)
                        loadImage(img, products[i].product.product_img[0])
                        rv?.addView(layout2)*/
                    }
                    //rv?.invalidate()
                }
                var orderProductImagesAdapter = GenericAdapter(
                    R.layout.item_order_productimage,
                    imagesArray
                )
                rv?.adapter = orderProductImagesAdapter
            }
        }

        @BindingAdapter("setTrackOrderVisibility")
        @JvmStatic
        fun setTrackOrderVisibility(view: View, order: ModelOrder) {
            if (order.status != null) {
                if (order.tracking_url.isNullOrEmpty()) {
                    view.hide()
                } else {
                    if (order.status.lowercase()
                            .contains(TrollaConstants.ORDERSTATUS_IN_TRANSIT)
                    ) {
                        view.show()
                    } else {
                        view.hide()
                    }
                }
            } else {
                view.hide()
            }
        }

    }
}