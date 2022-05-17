package com.trolla.healthsdk.feature_auth.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_auth.data.AuthRepository
import com.trolla.healthsdk.feature_auth.data.models.GetOTPResponse

class VerifyOTPOnMobileUsecase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        mobile: String
    ): Resource<BaseApiResponse<GetOTPResponse>> {
        return authRepository.getOTP(email, mobile)
    }
}