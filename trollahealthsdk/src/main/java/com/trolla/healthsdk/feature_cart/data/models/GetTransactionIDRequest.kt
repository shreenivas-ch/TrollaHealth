package com.trolla.healthsdk.feature_cart.data.models

data class GetTransactionIDRequest(
    var order_id: String,
    var payment_mode: String,
)