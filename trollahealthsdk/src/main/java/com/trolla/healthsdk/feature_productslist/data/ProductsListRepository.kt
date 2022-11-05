package com.trolla.healthsdk.feature_productslist.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse

interface ProductsListRepository {
    suspend fun getProductsList(
        page: String,
        limit: String,
        filterValue: String,
        filterBy: String
    ): Resource<BaseApiResponse<ProductsListResponse>>
}