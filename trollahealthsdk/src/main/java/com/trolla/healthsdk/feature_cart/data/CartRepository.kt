package com.trolla.healthsdk.feature_cart.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_cart.data.models.CreateOrderResponse
import com.trolla.healthsdk.feature_cart.data.models.OrderRequestModel

interface CartRepository {
    suspend fun getCartDetails(): Resource<BaseApiResponse<GetCartDetailsResponse>>
    suspend fun addToCart(
        product_id: Int,
        qty: Int
    ): Resource<BaseApiResponse<AddToCartResponse>>
    suspend fun createOrder(orderRequestModel: OrderRequestModel): Resource<BaseApiResponse<CreateOrderResponse>>
}