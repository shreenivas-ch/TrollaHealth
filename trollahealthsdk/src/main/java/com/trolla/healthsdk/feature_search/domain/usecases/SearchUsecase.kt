package com.trolla.healthsdk.feature_search.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_search.data.SearchRepository
import com.trolla.healthsdk.feature_search.data.SearchResponse

class SearchUsecase(private val searchRepository: SearchRepository) {
    suspend operator fun invoke(
        query: String, page: String, limit: String
    ): Resource<BaseApiResponse<SearchResponse>> {
        return searchRepository.search(query, page, limit)
    }
}