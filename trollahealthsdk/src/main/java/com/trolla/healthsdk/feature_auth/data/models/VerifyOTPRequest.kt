package com.trolla.healthsdk.feature_auth.data.models

data class VerifyOTPRequest(
    var email: String,
    var mobile: String,
    var otp: String
)