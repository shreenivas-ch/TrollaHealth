package com.trolla.healthsdk.feature_orders.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_cart.data.models.CreateOrderResponse
import com.trolla.healthsdk.feature_cart.data.models.OrderRequestModel

interface OrdersRepository {
    suspend fun getOrdersList(): Resource<BaseApiResponse<GetOrdersListResponse>>
    suspend fun getOrderDetails(id:String): Resource<BaseApiResponse<OrderDetailsResponse>>
    suspend fun cancelOrder(id:String): Resource<BaseApiResponse<CommonAPIResponse>>
}