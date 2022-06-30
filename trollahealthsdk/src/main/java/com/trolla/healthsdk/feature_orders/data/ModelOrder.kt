package com.trolla.healthsdk.feature_orders.data

import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse

data class ModelOrder(
    val _id: String,
    val products: ArrayList<GetCartDetailsResponse.CartProduct>,
    val amount: String,
    val status: String,
    val order_id: String,

    )