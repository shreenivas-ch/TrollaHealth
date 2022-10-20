package com.trolla.healthsdk.feature_auth.data.models

data class VerifyOTPResponse(
    val access_token: String,
    val is_profile_complete: String
)