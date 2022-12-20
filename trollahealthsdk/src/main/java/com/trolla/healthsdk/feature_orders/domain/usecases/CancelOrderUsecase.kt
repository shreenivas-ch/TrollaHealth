package com.trolla.healthsdk.feature_orders.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_orders.data.OrdersRepository

class CancelOrderUsecase(private val ordersRepository: OrdersRepository) {
    suspend operator fun invoke(
        id:String,wf_order_id:String
    ): Resource<BaseApiResponse<CommonAPIResponse>> {
        return ordersRepository.cancelOrder(id,wf_order_id)
    }
}