package com.trolla.healthsdk.feature_orders.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_cart.data.models.GetTransactionIDRequest
import com.trolla.healthsdk.feature_cart.data.models.GetTransactionIDResponse
import com.trolla.healthsdk.feature_cart.domain.usecases.GetTransactionIDUsecase
import com.trolla.healthsdk.feature_orders.data.GetOrdersListResponse
import com.trolla.healthsdk.feature_orders.data.OrderDetailsResponse
import com.trolla.healthsdk.feature_orders.domain.usecases.CancelOrderUsecase
import com.trolla.healthsdk.feature_orders.domain.usecases.GetOrderDetailsUsecase
import com.trolla.healthsdk.feature_orders.domain.usecases.GetOrdersListUsecase
import com.trolla.healthsdk.feature_orders.domain.usecases.RepeatOrderUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import kotlinx.coroutines.launch

class OrderDetailsViewModel(
    val getOrderDetailsUsecase: GetOrderDetailsUsecase,
    val getTransactionIDUsecase: GetTransactionIDUsecase,
    val cancelOrderUsecase: CancelOrderUsecase,
    val repeatOrderUsecase: RepeatOrderUsecase
) : BaseViewModel() {

    val orderDetailsResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<OrderDetailsResponse>>>()
    val cancelOrderResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<CommonAPIResponse>>>()
    val repeatOrderResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<JsonElement>>>()
    val getTransactionIDLiveData =
        MutableLiveData<Resource<BaseApiResponse<GetTransactionIDResponse>>>()

    fun getOrderDetails(orderid: String,wf_order_id:String) {
        progressStatus.value = true
        viewModelScope.launch {
            orderDetailsResponseLiveData.value =
                getOrderDetailsUsecase(orderid,wf_order_id)!!
            progressStatus.value = false
        }
    }

    fun cancelOrder(orderid: String,wf_order_id:String) {
        progressStatus.value = true
        viewModelScope.launch {
            cancelOrderResponseLiveData.value =
                cancelOrderUsecase(orderid,wf_order_id)!!
            progressStatus.value = false
        }
    }

    fun repeatOrder(orderid: String) {
        progressStatus.value = true
        viewModelScope.launch {
            repeatOrderResponseLiveData.value =
                repeatOrderUsecase(orderid)!!
            progressStatus.value = false
        }
    }

    fun getTransactionID(orderid: String, paymentMode: String) {
        var getTransactionIDRequest = GetTransactionIDRequest(orderid, paymentMode)

        progressStatus.value = true
        viewModelScope.launch {
            getTransactionIDLiveData.value =
                getTransactionIDUsecase(getTransactionIDRequest)!!
            progressStatus.value = false
        }
    }

    val profileNameLiveData = MutableLiveData<String>()
    val profileEmailLiveData = MutableLiveData<String>()
    val profileMobileLiveData = MutableLiveData<String>()
    fun getProfile() {
        viewModelScope.launch {
            profileNameLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_NAME)
            profileEmailLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_EMAIL)
            profileMobileLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_MOBILE)
        }
    }
}