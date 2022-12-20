package com.trolla.healthsdk.feature_payment.data

data class ModelTransaction(
    val _id: String,
    val status: String,
    val mode: String,
    val amount: String
)