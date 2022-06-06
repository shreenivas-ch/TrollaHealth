package com.trolla.healthsdk.feature_cart.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse

interface CartRepository {
    suspend fun getCartDetails():Resource<BaseApiResponse<GetCartDetailsResponse>>
    suspend fun addToCart():Resource<BaseApiResponse<AddToCartResponse>>
}