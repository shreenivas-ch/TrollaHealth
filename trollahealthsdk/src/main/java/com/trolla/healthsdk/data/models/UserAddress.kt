package com.trolla.healthsdk.data.models

data class UserAddress(
    var addressline1: String,
    var addressline2: String,
    var landmark: String,
    var city: String,
    var state: String,
    var pincode: String
)