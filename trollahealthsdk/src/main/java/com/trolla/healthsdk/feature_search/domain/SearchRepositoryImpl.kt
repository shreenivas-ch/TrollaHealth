package com.trolla.healthsdk.feature_search.domain

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
import com.trolla.healthsdk.feature_search.data.SearchRepository
import com.trolla.healthsdk.feature_search.data.SearchResponse

class SearchRepositoryImpl(private val apiService: ApiService) : SearchRepository {

    override suspend fun search(query: String,page:String, limit:String): Resource<BaseApiResponse<SearchResponse>> {
        val response = apiService.search(query,page,limit)
        val resource = APIErrorHandler<SearchResponse>().process(response)
        return resource
    }
}

fun provideSearchRepository(apiService: ApiService): SearchRepository {
    return SearchRepositoryImpl(apiService)
}