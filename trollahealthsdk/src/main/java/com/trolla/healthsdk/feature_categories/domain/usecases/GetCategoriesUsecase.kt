package com.trolla.healthsdk.feature_categories.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_categories.data.CategoriesResponse
import com.trolla.healthsdk.feature_categories.data.CategoryRepository

class GetCategoriesUsecase(private val categoriesRepository: CategoryRepository) {
    suspend operator fun invoke(
    ): Resource<BaseApiResponse<CategoriesResponse>> {
        return categoriesRepository.getCategories()
    }
}