package com.trolla.healthsdk.feature_auth.data.models

data class VerifyOTPRequest(
    var identifier: String,
    var otp: String
)