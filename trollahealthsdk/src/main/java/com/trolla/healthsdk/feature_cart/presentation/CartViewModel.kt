package com.trolla.healthsdk.feature_cart.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_cart.data.AddToCartResponse
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.data.models.CreateOrderResponse
import com.trolla.healthsdk.feature_cart.data.models.GetTransactionIDRequest
import com.trolla.healthsdk.feature_cart.data.models.OrderRequestModel
import com.trolla.healthsdk.feature_cart.data.models.PaymentUpdateRequest
import com.trolla.healthsdk.feature_cart.domain.usecases.AddToCartUsercase
import com.trolla.healthsdk.feature_cart.domain.usecases.CreateOrderUsecase
import com.trolla.healthsdk.feature_cart.domain.usecases.GetCartDetailsUsecase
import com.trolla.healthsdk.feature_cart.domain.usecases.UpdatePaymentUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.utils.TrollaConstants
import kotlinx.coroutines.launch
import org.json.JSONObject

class CartViewModel(
    private val addToCartUsercase: AddToCartUsercase,
    private val getCartDetailsUsecase: GetCartDetailsUsecase,
    private val createOrderUsecase: CreateOrderUsecase,
    private val updatePaymentUsecase: UpdatePaymentUsecase
) : BaseViewModel() {

    val selectedAddressIdLiveData = MutableLiveData<String>("")
    val selectedPaymentModeLiveData = MutableLiveData<String>("")
    val isCartValid = MutableLiveData<Boolean>(false)

    val addToCartResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<AddToCartResponse>>>()

    val cartDetailsResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<GetCartDetailsResponse>>>()

    val createOrderResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<CreateOrderResponse>>>()

    val updatePaymentResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<CommonAPIResponse>>>()

    fun addToCart(
        product_id: Int, qty: Int,
        type: String = TrollaConstants.ADDTOCART_TYPE_PRODUCT,
        prescriptions: ArrayList<String> = arrayListOf()
    ) {
        progressStatus.value = true
        viewModelScope.launch {
            addToCartResponseLiveData.value =
                addToCartUsercase(product_id, qty, type, prescriptions)!!
            progressStatus.value = false
        }
    }

    fun getCartDetails() {
        progressStatus.value = true
        viewModelScope.launch {
            cartDetailsResponseLiveData.value =
                getCartDetailsUsecase()!!
            progressStatus.value = false
        }
    }

    fun placeOrder() {
        var createOrderRequestModel = OrderRequestModel(
            selectedPaymentModeLiveData.value ?: "",
            selectedAddressIdLiveData.value ?: ""
        )
        progressStatus.value = true
        viewModelScope.launch {
            createOrderResponseLiveData.value =
                createOrderUsecase(createOrderRequestModel)!!
            progressStatus.value = false
        }
    }

    fun updatePayment(transactionid: String, data: JSONObject, paymentStatus: String) {
        var paymentUpdateRequest = PaymentUpdateRequest(data, paymentStatus)

        progressStatus.value = true
        viewModelScope.launch {
            updatePaymentResponseLiveData.value =
                updatePaymentUsecase(transactionid, paymentUpdateRequest)!!
            progressStatus.value = false
        }
    }
}