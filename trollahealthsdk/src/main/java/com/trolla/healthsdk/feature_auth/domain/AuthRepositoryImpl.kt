package com.trolla.healthsdk.feature_auth.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.feature_auth.data.AuthRepository
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_auth.data.models.*

class AuthRepositoryImpl(private val apiService: ApiService) : AuthRepository {
    override suspend fun getOTP(
        email: String,
        mobile: String
    ): Resource<BaseApiResponse<CommonAPIResponse>> {
        var response = apiService.authGetOTP(GetOTPRequest(email))
        var resource = APIErrorHandler<CommonAPIResponse>().process(response)
        return resource
    }

    override suspend fun verifyOTP(
        identifier: String,
        otp: String
    ): Resource<BaseApiResponse<VerifyOTPResponse>> {
        var response = apiService.authVerifyOTP(VerifyOTPRequest(identifier,otp))
        var resource = APIErrorHandler<VerifyOTPResponse>().process(response)
        return resource
    }

    override suspend fun updateProfile(
        name: String,
        mobile: String,
        gender: String,
        day: String,
        month: String,
        year: String
    ): Resource<BaseApiResponse<UpdateProfileResponse>> {
        var response = apiService.updateProfile(UpdateProfileRequest(name, mobile, gender, day, month, year))
        var resource = APIErrorHandler<UpdateProfileResponse>().process(response)
        return resource
    }
}

fun provideAuthRepository(apiService: ApiService): AuthRepository {
    return AuthRepositoryImpl(apiService)
}