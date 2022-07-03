package com.trolla.healthsdk.feature_cart.data.models

data class AddToCartRequest(
    var product_id: Int,
    var qty: Int,
    var type: String = "product",  // -product,prescription
    var prescriptions: ArrayList<String> = arrayListOf()
)