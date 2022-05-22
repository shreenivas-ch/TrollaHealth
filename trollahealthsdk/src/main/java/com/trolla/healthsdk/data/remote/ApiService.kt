package com.trolla.healthsdk.data.remote

import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.data.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("/auth/get-otp")
    suspend fun authGetOTP(
        @Body request: GetOTPRequest
    ): Response<BaseApiResponse<CommonAPIResponse>>

    @POST("/auth/verify-otp")
    suspend fun authVerifyOTP(
        @Body request: VerifyOTPRequest
    ): Response<BaseApiResponse<VerifyOTPResponse>>

    @PUT("/users/profile")
    suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): Response<BaseApiResponse<UpdateProfileResponse>>
}