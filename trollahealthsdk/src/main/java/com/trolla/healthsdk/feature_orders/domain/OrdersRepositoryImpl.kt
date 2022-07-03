package com.trolla.healthsdk.feature_orders.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_cart.data.models.CreateOrderResponse
import com.trolla.healthsdk.feature_cart.data.models.OrderRequestModel
import com.trolla.healthsdk.feature_orders.data.*

class OrdersRepositoryImpl(private val apiService: ApiService) : OrdersRepository {

    override suspend fun getOrdersList(): Resource<BaseApiResponse<GetOrdersListResponse>> {
        val response = apiService.getOrders()
        return APIErrorHandler<GetOrdersListResponse>().process(response)
    }

    override suspend fun getOrderDetails(id: String): Resource<BaseApiResponse<OrderDetailsResponse>> {
        val response = apiService.getOrderDetails(id)
        return APIErrorHandler<OrderDetailsResponse>().process(response)
    }
}

fun provideOrderRepository(apiService: ApiService): OrdersRepository {
    return OrdersRepositoryImpl(apiService)
}