package com.trolla.healthsdk.feature_dashboard.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardRepository
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.utils.TrollaPreferencesManager

class DashboardRepositoryImpl(private val apiService: ApiService) : DashboardRepository {
    override suspend fun getDashboard(): Resource<BaseApiResponse<DashboardResponse>> {
        var pincode =
            TrollaPreferencesManager.getString(TrollaPreferencesManager.PM_DEFAULT_PINCODE) ?: ""
        val response = apiService.getDashboard(pincode)
        val resource = APIErrorHandler<DashboardResponse>().process(response)
        return resource
    }

    override suspend fun getProfile(): Resource<BaseApiResponse<UpdateProfileResponse>> {
        val response = apiService.getProfile()
        val resource = APIErrorHandler<UpdateProfileResponse>().process(response)
        return resource
    }
}

fun provideDashboardRepository(apiService: ApiService): DashboardRepository {
    return DashboardRepositoryImpl(apiService)
}