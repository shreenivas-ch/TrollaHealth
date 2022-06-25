package com.trolla.healthsdk.feature_productdetails.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse

interface ProductDetailsRepository {
    suspend fun getProductDetails(id:String): Resource<BaseApiResponse<GetProductDetailsResponse>>
}