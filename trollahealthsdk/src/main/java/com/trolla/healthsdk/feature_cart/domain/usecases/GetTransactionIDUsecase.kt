package com.trolla.healthsdk.feature_cart.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_cart.data.CartRepository
import com.trolla.healthsdk.feature_cart.data.models.*
import com.trolla.healthsdk.feature_orders.data.OrdersRepository

class GetTransactionIDUsecase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(
        getTransactionIDRequest: GetTransactionIDRequest
    ): Resource<BaseApiResponse<GetTransactionIDResponse>> {
        return cartRepository.getTrasactionID(getTransactionIDRequest)
    }
}