package com.trolla.healthsdk.feature_cart.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_cart.data.CartRepository
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse

class GetCartDetailsUsecase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(
    ): Resource<BaseApiResponse<GetCartDetailsResponse>> {
        return cartRepository.getCartDetails()
    }
}