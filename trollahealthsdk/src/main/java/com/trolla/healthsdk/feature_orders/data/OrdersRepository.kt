package com.trolla.healthsdk.feature_orders.data

import com.google.gson.JsonElement
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse

interface OrdersRepository {
    suspend fun getOrdersList(): Resource<BaseApiResponse<GetOrdersListResponse>>
    suspend fun getOrderDetails(id:String,wf_order_id:String): Resource<BaseApiResponse<OrderDetailsResponse>>
    suspend fun cancelOrder(id:String,wf_order_id:String): Resource<BaseApiResponse<CommonAPIResponse>>
    suspend fun repeatOrder(id:String): Resource<BaseApiResponse<JsonElement>>
}