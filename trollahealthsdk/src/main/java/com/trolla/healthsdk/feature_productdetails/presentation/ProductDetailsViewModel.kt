package com.trolla.healthsdk.feature_productdetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productdetails.domain.usecases.GetProductDetailsUsecase
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.utils.LogUtil
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val productDetailsUsecase: GetProductDetailsUsecase) :
    BaseViewModel() {
    val getProductDetailsResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<GetProductDetailsResponse>>>()

    var dashboardProduct = MutableLiveData<DashboardResponse.DashboardProduct>()

    fun getProductDetails(id: String) {
        progressStatus.value = true
        viewModelScope.launch {
            var response = productDetailsUsecase(id)!!
            getProductDetailsResponseLiveData.value = response
            progressStatus.value = false
        }
    }
}