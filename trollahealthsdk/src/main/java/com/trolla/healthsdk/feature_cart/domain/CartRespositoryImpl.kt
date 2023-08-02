package com.trolla.healthsdk.feature_cart.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.data.remote.ApiService2
import com.trolla.healthsdk.feature_cart.data.AddToCartResponse
import com.trolla.healthsdk.feature_cart.data.CartRepository
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.data.models.*
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import com.trolla.healthsdk.utils.TrollaPreferencesManager.PM_DEFAULT_PINCODE

class CartRespositoryImpl(
    private val apiService: ApiService,
    private val apiService2: ApiService2
) : CartRepository {
    override suspend fun getCartDetails(): Resource<BaseApiResponse<GetCartDetailsResponse>> {
        val response = apiService.getCartDetails(
            TrollaPreferencesManager.getString(PM_DEFAULT_PINCODE) ?: ""
        )
        return APIErrorHandler<GetCartDetailsResponse>().process(response)
    }

    override suspend fun addToCart(
        product_id: Int,
        qty: Int,
        type: String,
        prescriptions: ArrayList<String>
    ): Resource<BaseApiResponse<AddToCartResponse>> {

        var cartRequest = AddToCartRequest(
            product_id,
            qty,
            type,
            prescriptions
        )

        val response = apiService2.addToCart(cartRequest)
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

    override suspend fun getTrasactionID(
        getTransactionIDRequest: GetTransactionIDRequest
    ): Resource<BaseApiResponse<GetTransactionIDResponse>> {
        val response = apiService.getTransactionID(getTransactionIDRequest)
        return APIErrorHandler<GetTransactionIDResponse>().process(response)
    }
}

fun provideCartRepository(apiService: ApiService, apiService2: ApiService2): CartRepository {
    return CartRespositoryImpl(apiService, apiService2)
}