package com.trolla.healthsdk.feature_orders.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_orders.data.GetOrdersListResponse
import com.trolla.healthsdk.feature_orders.domain.usecases.GetOrdersListUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import kotlinx.coroutines.launch

class OrderListViewModel(val getOrdersListUsecase: GetOrdersListUsecase) : BaseViewModel() {

    val orderListReposponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<GetOrdersListResponse>>>()

    fun getOrdersList() {
        progressStatus.value = true
        viewModelScope.launch {
            orderListReposponseLiveData.value =
                getOrdersListUsecase()!!
            progressStatus.value = false
        }
    }
}