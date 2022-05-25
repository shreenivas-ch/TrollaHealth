package com.trolla.healthsdk.feature_dashboard.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.data.AuthRepository

class GetDashboardUsecase(private val dashboardRepository: DashboardRepository) {
    suspend operator fun invoke(
    ): Resource<BaseApiResponse<CommonAPIResponse>> {
        return authRepository.getOTP(email, mobile)
    }
}