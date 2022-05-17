package com.trolla.healthsdk.data.remote

import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.data.models.GetOTPRequest
import com.trolla.healthsdk.feature_auth.data.models.VerifyOTPRequest
import com.trolla.healthsdk.feature_auth.data.models.VerifyOTPResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/auth/get-otp")
    suspend fun authGetOTP(
        @Body request: GetOTPRequest
    ): Response<BaseApiResponse<CommonAPIResponse>>

    @POST("/auth/get-otp")
    suspend fun authVerifyOTP(
        @Body request: VerifyOTPRequest
    ): Response<BaseApiResponse<VerifyOTPResponse>>
}