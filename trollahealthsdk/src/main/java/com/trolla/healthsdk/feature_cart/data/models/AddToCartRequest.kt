package com.trolla.healthsdk.feature_cart.data.models

data class AddToCartRequest(
    var product_id: String,
    var qty: String
)