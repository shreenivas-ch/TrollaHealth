package com.trolla.healthsdk.feature_auth.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_auth.data.models.VerifyOTPResponse

interface AuthRepository {
    suspend fun getOTP(email: String, mobile: String): Resource<BaseApiResponse<CommonAPIResponse>>

    suspend fun verifyOTP(
        identifier: String,
        otp: String
    ): Resource<BaseApiResponse<VerifyOTPResponse>>

    suspend fun updateProfile(
        name: String,
        mobile: String,
        gender: String,
        day: String,
        month: String,
        year: String
    ): Resource<BaseApiResponse<UpdateProfileResponse>>
}