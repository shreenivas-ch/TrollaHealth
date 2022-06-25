package com.trolla.healthsdk.feature_cart.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_cart.data.AddToCartResponse
import com.trolla.healthsdk.feature_cart.data.CartRepository

class AddToCartUsercase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(
        product_id: Int, qty: Int
    ): Resource<BaseApiResponse<AddToCartResponse>> {
        return cartRepository.addToCart(product_id, qty)
    }
}