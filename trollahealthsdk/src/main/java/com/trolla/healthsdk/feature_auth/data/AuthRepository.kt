package com.trolla.healthsdk.feature_auth.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.data.models.VerifyOTPResponse

interface AuthRepository {
    suspend fun getOTP(email: String, mobile: String): Resource<BaseApiResponse<CommonAPIResponse>>
    suspend fun verifyOTP(
        email: String,
        mobile: String,
        otp: String
    ): Resource<BaseApiResponse<VerifyOTPResponse>>
}