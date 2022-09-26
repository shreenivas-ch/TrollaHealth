package com.trolla.healthsdk.feature_orders.data

import com.trolla.healthsdk.core.ListItemViewModel
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_payment.data.ModelTransaction

data class ModelOrder(
    val _id: String,
    val products: ArrayList<GetCartDetailsResponse.CartProduct>,
    val prescriptions: ArrayList<String>,
    val order_value: OrderValue,
    val status: String,
    val tracking_url: String,
    val eta: String,
    val created_at: String,
    val order_id: String,
    val address: ModelAddress,
    val transactions: ArrayList<ModelTransaction>
) : ListItemViewModel() {

    data class OrderValue(
        val totalValue: String,
        val totalValueAfterDisc: String,
        val gst: String,
        val totalDiscount: String,
        val deliveryFees: String,
        val payable: String,
    )
}