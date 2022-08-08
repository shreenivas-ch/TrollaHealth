package com.trolla.healthsdk.feature_cart.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_cart.data.AddToCartResponse
import com.trolla.healthsdk.feature_cart.data.CartRepository
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.data.models.AddToCartRequest
import com.trolla.healthsdk.feature_cart.data.models.CreateOrderResponse
import com.trolla.healthsdk.feature_cart.data.models.OrderRequestModel
import com.trolla.healthsdk.feature_cart.data.models.PaymentUpdateRequest
import retrofit2.Response

class CartRespositoryImpl(private val apiService: ApiService) : CartRepository {
    override suspend fun getCartDetails(): Resource<BaseApiResponse<GetCartDetailsResponse>> {
        val response = apiService.getCartDetails()
        return APIErrorHandler<GetCartDetailsResponse>().process(response)
    }

    override suspend fun addToCart(
        product_id: Int,
        qty: Int,
        type: String,
        prescriptions: ArrayList<String>
    ): Resource<BaseApiResponse<AddToCartResponse>> {

        var cartRequest = AddToCartRequest(product_id, qty, type, prescriptions)

        val response = apiService.addToCart(cartRequest)
        return APIErrorHandler<AddToCartResponse>().process(response)
    }

    override suspend fun createOrder(orderRequestModel: OrderRequestModel): Resource<BaseApiResponse<CreateOrderResponse>> {
        val response = apiService.createOrder(orderRequestModel)
        return APIErrorHandler<CreateOrderResponse>().process(response)
    }

    override suspend fun updatePayment(
        transactionid: String,
        paymentUpdateRequest: PaymentUpdateRequest
    ): Resource<BaseApiResponse<CommonAPIResponse>> {
        val response = apiService.updatePayment(transactionid, paymentUpdateRequest)
        return APIErrorHandler<CommonAPIResponse>().process(response)
    }
}

fun provideCartRepository(apiService: ApiService): CartRepository {
    return CartRespositoryImpl(apiService)
}