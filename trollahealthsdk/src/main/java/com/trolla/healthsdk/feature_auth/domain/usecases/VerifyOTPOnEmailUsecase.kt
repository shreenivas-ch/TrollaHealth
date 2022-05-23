package com.trolla.healthsdk.feature_auth.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_auth.data.AuthRepository
import com.trolla.healthsdk.feature_auth.data.models.VerifyOTPResponse

class VerifyOTPOnEmailUsecase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        identifier: String,
        otp: String
    ): Resource<BaseApiResponse<VerifyOTPResponse>> {
        return authRepository.verifyOTP(identifier, otp)
    }
}