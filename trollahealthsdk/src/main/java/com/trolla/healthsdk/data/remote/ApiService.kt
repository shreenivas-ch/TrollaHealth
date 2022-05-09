package com.trolla.healthsdk.data.remote

import com.trolla.healthsdk.feature_auth.data.models.LoginResponse
import com.trolla.healthsdk.data.models.BasicApiResponse
import com.trolla.healthsdk.feature_auth.data.models.GetOTPRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("/auth/get-otp")
    suspend fun authGetOTP(
        @Body request: GetOTPRequest
    ): BasicApiResponse<LoginResponse>
}