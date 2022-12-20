package com.trolla.healthsdk.feature_cart.data.models

import org.json.JSONObject

data class PaymentUpdateRequest(
    var payment_response: JSONObject,
    var status: String,
)