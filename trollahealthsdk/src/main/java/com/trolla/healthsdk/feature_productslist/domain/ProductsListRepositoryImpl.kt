package com.trolla.healthsdk.feature_productslist.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_auth.data.AuthRepository
import com.trolla.healthsdk.feature_auth.domain.AuthRepositoryImpl
import com.trolla.healthsdk.feature_dashboard.data.DashboardRepository
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

class DashboardRepositoryImpl(private val apiService: ApiService) : DashboardRepository {
    override suspend fun getDashboard(): Resource<BaseApiResponse<DashboardResponse>> {
        val response = apiService.getDashboard()
        val resource = APIErrorHandler<DashboardResponse>().process(response)
        return resource
    }
}

fun provideDashboardRepository(apiService: ApiService): DashboardRepository {
    return DashboardRepositoryImpl(apiService)
}