package com.trolla.healthsdk.feature_cart.data

import com.trolla.healthsdk.core.ListItemViewModel
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_prescriptionupload.data.ModelPrescription

class GetCartDetailsResponse(
    val cart: Cart,
    val payment_options: ArrayList<PaymentOptions>,
) {
    data class Cart(
        val cartValue: String,
        val cartValueAfterDisc: String,
        val gst: String,
        val totalDiscount: String,
        val deliveryFees: String,
        val payable: String,
        val products: ArrayList<CartProduct>,
        val prescriptions: ArrayList<String>
    )

    data class CartProduct(
        val product: DashboardResponse.DashboardProduct,
        val qty: Int
    ) : ListItemViewModel()

    data class PaymentOptions(
        val payment_mode: String,
        val name: String
    )
}