package com.trolla.healthsdk.feature_productdetails.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productdetails.data.ProductDetailsRepository
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import com.trolla.healthsdk.utils.TrollaPreferencesManager.PM_DEFAULT_PINCODE

class ProductDetailsRepositoryImpl(private val apiService: ApiService) : ProductDetailsRepository {
    override suspend fun getProductDetails(id:String): Resource<BaseApiResponse<GetProductDetailsResponse>> {
        val response = apiService.getProductDetails(id,
            TrollaPreferencesManager.get<String>(PM_DEFAULT_PINCODE) ?: "")
        return APIErrorHandler<GetProductDetailsResponse>().process(response)
    }
}

fun provideProductDetailsRepository(apiService: ApiService): ProductDetailsRepository {
    return ProductDetailsRepositoryImpl(apiService)
}