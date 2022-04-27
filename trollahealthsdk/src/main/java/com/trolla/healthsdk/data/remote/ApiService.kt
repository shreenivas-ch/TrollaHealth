package com.trolla.healthsdk.data.remote

import com.trolla.healthsdk.feature_auth.data.models.LoginResponse
import com.trolla.healthsdk.data.models.BasicApiResponse
import retrofit2.http.GET

interface ApiService {

    @GET("/get-otp")
    suspend fun authGetOTP(email: String, mobile: String): BasicApiResponse<LoginResponse>
}