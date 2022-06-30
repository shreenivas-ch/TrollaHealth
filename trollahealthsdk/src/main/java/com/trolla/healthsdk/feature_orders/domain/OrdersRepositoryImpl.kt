package com.trolla.healthsdk.feature_orders.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_address.data.*
import com.trolla.healthsdk.feature_orders.data.*
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productdetails.data.ProductDetailsRepository

class OrdersRepositoryImpl(private val apiService: ApiService) : OrdersRepository {

    override suspend fun getOrdersList(): Resource<BaseApiResponse<GetOrdersListResponse>> {
        val response = apiService.getOrders()
        return APIErrorHandler<GetOrdersListResponse>().process(response)
    }

    override suspend fun createOrder(orderRequestModel: OrderRequestModel): Resource<BaseApiResponse<CreateOrderResponse>> {
        val response = apiService.createOrder(orderRequestModel)
        return APIErrorHandler<CreateOrderResponse>().process(response)
    }

    override suspend fun getOrderDetails(id: String): Resource<BaseApiResponse<OrderDetailsResponse>> {
        val response = apiService.getOrderDetails(id)
        return APIErrorHandler<OrderDetailsResponse>().process(response)
    }
}

fun provideAddressRepository(apiService: ApiService): OrdersRepository {
    return OrdersRepositoryImpl(apiService)
}