package com.trolla.healthsdk.feature_productslist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse
import com.trolla.healthsdk.feature_productslist.domain.usecases.GetProductsListUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import kotlinx.coroutines.launch

class ProductsListViewModel(val getProductsListUsecase: GetProductsListUsecase) : BaseViewModel() {

    val productsListResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<ProductsListResponse>>>()

    fun getProductsList(page: String, limit: String, filterValue: String, filterBy: String) {
        progressStatus.value = true
        viewModelScope.launch {
            productsListResponseLiveData.value =
                getProductsListUsecase(page, limit, filterValue, filterBy)!!
            progressStatus.value = false
        }
    }
}