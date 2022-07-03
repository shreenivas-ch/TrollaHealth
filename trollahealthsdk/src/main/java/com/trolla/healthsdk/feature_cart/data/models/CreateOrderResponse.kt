package com.trolla.healthsdk.feature_cart.data.models

import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_orders.data.ModelOrder
import com.trolla.healthsdk.feature_payment.data.ModelTransaction

class CreateOrderResponse(
    val order: ModelOrder,
    val transaction: ModelTransaction
)