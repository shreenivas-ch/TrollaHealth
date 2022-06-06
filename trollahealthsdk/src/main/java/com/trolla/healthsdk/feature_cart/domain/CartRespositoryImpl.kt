package com.trolla.healthsdk.feature_cart.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_cart.data.AddToCartResponse
import com.trolla.healthsdk.feature_cart.data.CartRepository
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse

class CartRespositoryImpl(private val apiService: ApiService) : CartRepository {
    override suspend fun getCartDetails(): Resource<BaseApiResponse<GetCartDetailsResponse>> {
        val response = apiService.getCartDetails()
        return APIErrorHandler<GetCartDetailsResponse>().process(response)
    }

    override suspend fun addToCart(): Resource<BaseApiResponse<AddToCartResponse>> {
        val response = apiService.addToCart()
        return APIErrorHandler<AddToCartResponse>().process(response)
    }
}

fun provideCartRepository(apiService: ApiService): CartRepository {
    return CartRespositoryImpl(apiService)
}