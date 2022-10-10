package com.trolla.healthsdk.feature_search.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_search.data.SearchRepository
import com.trolla.healthsdk.feature_search.data.SearchResponse
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import com.trolla.healthsdk.utils.TrollaPreferencesManager.PM_DEFAULT_PINCODE

class SearchRepositoryImpl(private val apiService: ApiService) : SearchRepository {

    override suspend fun search(
        query: String,
        page: String,
        limit: String
    ): Resource<BaseApiResponse<SearchResponse>> {
        val response = apiService.search(
            query, page, limit,
            TrollaPreferencesManager.get<String>(PM_DEFAULT_PINCODE) ?: ""
        )
        val resource = APIErrorHandler<SearchResponse>().process(response)
        return resource
    }
}

fun provideSearchRepository(apiService: ApiService): SearchRepository {
    return SearchRepositoryImpl(apiService)
}