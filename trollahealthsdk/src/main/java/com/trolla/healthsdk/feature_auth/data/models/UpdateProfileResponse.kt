package com.trolla.healthsdk.feature_auth.data.models

data class UpdateProfileResponse(
    val is_profile_complete: Boolean,
    val _id: String,
    val name: String,
    val email: String,
    val day: String,
    val gender: String,
    val mobile: String,
    val month: String,
    val year: String
)