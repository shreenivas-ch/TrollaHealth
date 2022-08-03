package com.trolla.healthsdk.feature_categories.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_categories.data.CategoriesResponse
import com.trolla.healthsdk.feature_categories.data.CategoryRepository
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardRepository

class GetCategoriesUsecase(private val categoriesRepository: CategoryRepository) {
    suspend operator fun invoke(
    ): Resource<BaseApiResponse<CategoriesResponse>> {
        return categoriesRepository.getCategories()
    }
}