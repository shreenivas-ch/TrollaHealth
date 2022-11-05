package com.trolla.healthsdk.feature_orders.domain.usecases

import com.google.gson.JsonElement
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_orders.data.OrdersRepository

class RepeatOrderUsecase(private val ordersRepository: OrdersRepository) {
    suspend operator fun invoke(
        id:String
    ): Resource<BaseApiResponse<JsonElement>> {
        return ordersRepository.repeatOrder(id)
    }
}