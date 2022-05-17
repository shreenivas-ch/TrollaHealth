package com.trolla.healthsdk.feature_auth.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.data.AuthRepository

class GetOTPOnEmailUsecase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        mobile: String
    ): Resource<BaseApiResponse<CommonAPIResponse>> {
        return authRepository.getOTP(email, mobile)
    }
}