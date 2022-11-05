package com.trolla.healthsdk.feature_search.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse

interface SearchRepository {
    suspend fun search(
        query: String,
        page: String,
        limit: String
    ): Resource<BaseApiResponse<SearchResponse>>
}