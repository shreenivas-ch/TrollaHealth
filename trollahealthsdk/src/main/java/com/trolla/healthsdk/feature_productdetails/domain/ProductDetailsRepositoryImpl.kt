package com.trolla.healthsdk.feature_productdetails.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productdetails.data.ProductDetailsRepository

class ProductDetailsRepositoryImpl(private val apiService: ApiService) : ProductDetailsRepository {
    override suspend fun getProductDetails(): Resource<BaseApiResponse<GetProductDetailsResponse>> {
        val response = apiService.getProductDetails()
        return APIErrorHandler<GetProductDetailsResponse>().process(response)
    }
}

fun provideProductDetailsRepository(apiService: ApiService): ProductDetailsRepository {
    return ProductDetailsRepositoryImpl(apiService)
}