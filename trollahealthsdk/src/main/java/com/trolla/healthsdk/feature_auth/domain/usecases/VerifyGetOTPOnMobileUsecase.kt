package com.trolla.healthsdk.feature_auth.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BasicApiResponse
import com.trolla.healthsdk.feature_auth.data.AuthRepository
import com.trolla.healthsdk.feature_auth.data.models.LoginResponse

class VerifyGetOTPOnMobileUsecase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        mobile: String
    ): Resource<BasicApiResponse<LoginResponse>> {
        return authRepository.login(email, mobile)
    }
}