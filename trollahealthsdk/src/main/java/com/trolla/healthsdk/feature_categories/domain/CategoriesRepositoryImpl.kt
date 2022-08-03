package com.trolla.healthsdk.feature_categories.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_auth.data.AuthRepository
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_auth.domain.AuthRepositoryImpl
import com.trolla.healthsdk.feature_categories.data.CategoriesResponse
import com.trolla.healthsdk.feature_categories.data.CategoryRepository
import com.trolla.healthsdk.feature_dashboard.data.DashboardRepository
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

class CategoriesRepositoryImpl(private val apiService: ApiService) : CategoryRepository {

    override suspend fun getCategories(): Resource<BaseApiResponse<CategoriesResponse>> {
        val response = apiService.getCategories()
        val resource = APIErrorHandler<CategoriesResponse>().process(response)
        return resource
    }
}

fun provideCategoriesRepository(apiService: ApiService): CategoryRepository {
    return CategoriesRepositoryImpl(apiService)
}