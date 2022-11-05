package com.trolla.healthsdk.feature_categories.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse

interface CategoryRepository {
    suspend fun getCategories(): Resource<BaseApiResponse<CategoriesResponse>>
}