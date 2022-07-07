package com.trolla.healthsdk.feature_orders.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_orders.data.GetOrdersListResponse
import com.trolla.healthsdk.feature_orders.data.OrderDetailsResponse
import com.trolla.healthsdk.feature_orders.domain.usecases.GetOrderDetailsUsecase
import com.trolla.healthsdk.feature_orders.domain.usecases.GetOrdersListUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import kotlinx.coroutines.launch

class OrderDetailsViewModel(val getOrderDetailsUsecase: GetOrderDetailsUsecase) : BaseViewModel() {

    val orderDetailsResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<OrderDetailsResponse>>>()

    fun getOrderDetails(orderid:String) {
        progressStatus.value = true
        viewModelScope.launch {
            orderDetailsResponseLiveData.value =
                getOrderDetailsUsecase(orderid)!!
            progressStatus.value = false
        }
    }
}