package com.trolla.healthsdk.feature_cart.data.models

data class OrderRequestModel(
    val payment_mode:String,
    val address_id:String
)