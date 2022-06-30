package com.trolla.healthsdk.feature_orders.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse

interface OrdersRepository {
    suspend fun getOrdersList(): Resource<BaseApiResponse<GetOrdersListResponse>>
    suspend fun createOrder(orderRequestModel: OrderRequestModel): Resource<BaseApiResponse<CreateOrderResponse>>
    suspend fun getOrderDetails(id:String): Resource<BaseApiResponse<OrderDetailsResponse>>
}