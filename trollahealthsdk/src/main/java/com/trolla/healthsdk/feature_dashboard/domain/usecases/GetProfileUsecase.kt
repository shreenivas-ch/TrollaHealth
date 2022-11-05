package com.trolla.healthsdk.feature_dashboard.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardRepository

class GetProfileUsecase(private val dashboardRepository: DashboardRepository) {
    suspend operator fun invoke(
    ): Resource<BaseApiResponse<UpdateProfileResponse>> {
        return dashboardRepository.getProfile()
    }
}