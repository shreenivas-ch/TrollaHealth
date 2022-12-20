package com.trolla.healthsdk.feature_cart.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_cart.data.models.*

interface CartRepository {
    suspend fun getCartDetails(): Resource<BaseApiResponse<GetCartDetailsResponse>>
    suspend fun addToCart(
        product_id: Int,
        qty: Int,
        type: String,
        prescriptions: ArrayList<String>
    ): Resource<BaseApiResponse<AddToCartResponse>>

    suspend fun createOrder(orderRequestModel: OrderRequestModel): Resource<BaseApiResponse<CreateOrderResponse>>
    suspend fun updatePayment(
        transactionid: String,
        paymentUpdateRequest: PaymentUpdateRequest
    ): Resource<BaseApiResponse<CommonAPIResponse>>

    suspend fun getTrasactionID(getTransactionIDRequest: GetTransactionIDRequest): Resource<BaseApiResponse<GetTransactionIDResponse>>
}