package com.trolla.healthsdk.data.remote

import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.data.models.GetOTPRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("/auth/get-otp")
    suspend fun authGetOTP(
        @Body request: GetOTPRequest
    ): Response<BaseApiResponse<CommonAPIResponse>>
}