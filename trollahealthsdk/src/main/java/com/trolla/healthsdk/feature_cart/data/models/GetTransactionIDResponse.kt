package com.trolla.healthsdk.feature_cart.data.models

class GetTransactionIDResponse(
    val transaction: OrderTransactionData
) {

    data class OrderTransactionData(
        val order: String,
        val status: String,
        val mode: String,
        val amount: String,
        val _id: String,
        val payment_gateway_ref: PaymentGatewayRef,
    )

    data class PaymentGatewayRef(
        val id: String,
        val gateway: String
    )
}