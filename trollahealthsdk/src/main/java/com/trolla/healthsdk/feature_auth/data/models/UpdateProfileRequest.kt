package com.trolla.healthsdk.feature_auth.data.models

data class UpdateProfileRequest(
    var name: String,
    var mobile: String,
    var gender: String,
    var day: String,
    var month: String,
    var year: String
)