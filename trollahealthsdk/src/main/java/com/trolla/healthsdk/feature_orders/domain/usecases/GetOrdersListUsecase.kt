package com.trolla.healthsdk.feature_orders.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_address.data.AddAddressResponse
import com.trolla.healthsdk.feature_address.data.AddressRepository
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_orders.data.GetOrdersListResponse
import com.trolla.healthsdk.feature_orders.data.OrdersRepository
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productdetails.data.ProductDetailsRepository
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse

class GetOrdersListUsecase(private val ordersRepository: OrdersRepository) {
    suspend operator fun invoke(
    ): Resource<BaseApiResponse<GetOrdersListResponse>> {
        return ordersRepository.getOrdersList()
    }
}