package com.trolla.healthsdk.feature_address.data

data class AddAddressRequest(
    val _id: String = "",
    val name: String = "",
    val contact: Long = 0L,
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val landmark: String = "",
    val pincode: Long = 0L,
    val type: String = "",
    val other: String = ""
)
