package com.trolla.healthsdk.feature_orders.data

import com.trolla.healthsdk.feature_payment.data.ModelTransaction

class CreateOrderResponse(
    val order: ArrayList<ModelOrder>,
    val transaction: ModelTransaction
)