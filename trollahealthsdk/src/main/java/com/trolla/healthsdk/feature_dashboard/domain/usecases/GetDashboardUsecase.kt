package com.trolla.healthsdk.feature_dashboard.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardRepository

class GetDashboardUsecase(private val dashboardRepository: DashboardRepository) {
    suspend operator fun invoke(
    ): Resource<BaseApiResponse<DashboardResponse>> {
        return dashboardRepository.getDashboard()
    }
}