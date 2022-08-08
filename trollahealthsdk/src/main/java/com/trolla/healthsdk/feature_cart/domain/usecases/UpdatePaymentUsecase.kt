package com.trolla.healthsdk.feature_cart.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_cart.data.CartRepository
import com.trolla.healthsdk.feature_cart.data.models.CreateOrderResponse
import com.trolla.healthsdk.feature_cart.data.models.OrderRequestModel
import com.trolla.healthsdk.feature_cart.data.models.PaymentUpdateRequest
import com.trolla.healthsdk.feature_orders.data.OrdersRepository

class UpdatePaymentUsecase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(
        transactionid: String, paymentUpdateRequest: PaymentUpdateRequest
    ): Resource<BaseApiResponse<CommonAPIResponse>> {
        return cartRepository.updatePayment(transactionid, paymentUpdateRequest)
    }
}