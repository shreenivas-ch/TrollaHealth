package com.trolla.healthsdk.feature_categories.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

interface CategoryRepository {
    suspend fun getCategories(): Resource<BaseApiResponse<CategoriesResponse>>
}