package com.trolla.healthsdk.feature_cart.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_cart.data.CartRepository
import com.trolla.healthsdk.feature_cart.data.models.CreateOrderResponse
import com.trolla.healthsdk.feature_cart.data.models.OrderRequestModel

class CreateOrderUsecase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(
        createOrderRequestModel: OrderRequestModel
    ): Resource<BaseApiResponse<CreateOrderResponse>> {
        return cartRepository.createOrder(createOrderRequestModel)
    }
}