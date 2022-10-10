package com.trolla.healthsdk.feature_productslist.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_productslist.data.ProductsListRepository
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import com.trolla.healthsdk.utils.TrollaPreferencesManager.PM_DEFAULT_PINCODE

class ProductsListRepositoryImpl(private val apiService: ApiService) : ProductsListRepository {
    override suspend fun getProductsList(
        page: String,
        limit: String,
        filterValue: String,
        filterBy: String
    ): Resource<BaseApiResponse<ProductsListResponse>> {
        val response = apiService.getProductsList(page, limit, filterValue, filterBy,
            TrollaPreferencesManager.get<String>(PM_DEFAULT_PINCODE) ?: "")
        val resource = APIErrorHandler<ProductsListResponse>().process(response)
        return resource
    }
}

fun provideProductsListRepository(apiService: ApiService): ProductsListRepository {
    return ProductsListRepositoryImpl(apiService)
}