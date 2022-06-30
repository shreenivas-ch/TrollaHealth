package com.trolla.healthsdk.feature_orders.data

data class OrderRequestModel(
    val payment_mode:String,
    val address_id:String
)