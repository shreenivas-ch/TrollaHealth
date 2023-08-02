package com.trolla.healthsdk.feature_orders.domain

import com.google.gson.JsonElement
import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.data.remote.ApiService2
import com.trolla.healthsdk.feature_orders.data.GetOrdersListResponse
import com.trolla.healthsdk.feature_orders.data.OrderDetailsResponse
import com.trolla.healthsdk.feature_orders.data.OrdersRepository
import com.trolla.healthsdk.feature_orders.data.RepeatOrderRequest

class OrdersRepositoryImpl(
    private val apiService: ApiService,
    private val apiService2: ApiService2
) : OrdersRepository {

    override suspend fun getOrdersList(): Resource<BaseApiResponse<GetOrdersListResponse>> {
        val response = apiService2.getOrders()
        return APIErrorHandler<GetOrdersListResponse>().process(response)
    }

    override suspend fun getOrderDetails(
        id: String,
        wf_order_id: String
    ): Resource<BaseApiResponse<OrderDetailsResponse>> {
        val response = apiService.getOrderDetails(id, wf_order_id)
        return APIErrorHandler<OrderDetailsResponse>().process(response)
    }

    override suspend fun cancelOrder(
        id: String,
        wf_order_id: String
    ): Resource<BaseApiResponse<CommonAPIResponse>> {
        val response = apiService.cancelOrder(id, wf_order_id)
        return APIErrorHandler<CommonAPIResponse>().process(response)
    }

    override suspend fun repeatOrder(id: String): Resource<BaseApiResponse<JsonElement>> {
        var repeatOrderRequest = RepeatOrderRequest(id)
        val response = apiService.repeatOrder(repeatOrderRequest)
        return APIErrorHandler<JsonElement>().process(response)
    }
}

fun provideOrderRepository(apiService: ApiService, apiService2: ApiService2): OrdersRepository {
    return OrdersRepositoryImpl(apiService, apiService2)
}