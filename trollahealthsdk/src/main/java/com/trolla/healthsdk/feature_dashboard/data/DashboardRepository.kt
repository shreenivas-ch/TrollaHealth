package com.trolla.healthsdk.feature_dashboard.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

interface DashboardRepository {
    suspend fun getDashboard(): Resource<BaseApiResponse<DashboardResponse>>
    suspend fun getProfile(): Resource<BaseApiResponse<UpdateProfileResponse>>
}