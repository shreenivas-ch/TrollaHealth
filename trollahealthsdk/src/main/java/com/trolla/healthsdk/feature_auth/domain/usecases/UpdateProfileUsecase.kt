package com.trolla.healthsdk.feature_auth.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_auth.data.AuthRepository
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse

class UpdateProfileUsecase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        name: String,
        mobile: String,
        gender: String,
        day: String,
        month: String,
        year: String
    ): Resource<BaseApiResponse<UpdateProfileResponse>> {
        return authRepository.updateProfile(name, mobile, gender, day, month, year)
    }
}