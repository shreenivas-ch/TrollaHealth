package com.trolla.healthsdk.feature_cart.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_cart.data.AddToCartResponse
import com.trolla.healthsdk.feature_cart.data.CartRepository
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.data.models.AddToCartRequest
import retrofit2.Response

class CartRespositoryImpl(private val apiService: ApiService) : CartRepository {
    override suspend fun getCartDetails(): Resource<BaseApiResponse<GetCartDetailsResponse>> {
        val response = apiService.getCartDetails()
        return APIErrorHandler<GetCartDetailsResponse>().process(response)
    }

    override suspend fun addToCart(
        product_id: Int,
        qty: Int
    ): Resource<BaseApiResponse<AddToCartResponse>> {

        var cartRequest = AddToCartRequest(product_id, qty)

        val response = apiService.addToCart(cartRequest)
        return APIErrorHandler<AddToCartResponse>().process(response)
    }
}

fun provideCartRepository(apiService: ApiService): CartRepository {
    return CartRespositoryImpl(apiService)
}