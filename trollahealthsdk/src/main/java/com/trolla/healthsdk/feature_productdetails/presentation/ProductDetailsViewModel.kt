package com.trolla.healthsdk.feature_productdetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_cart.data.AddToCartResponse
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.domain.usecases.AddToCartUsercase
import com.trolla.healthsdk.feature_cart.domain.usecases.GetCartDetailsUsecase
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productdetails.domain.usecases.GetProductDetailsUsecase
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.utils.LogUtil
import com.trolla.healthsdk.utils.TrollaConstants
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productDetailsUsecase: GetProductDetailsUsecase,
    private val getCartDetailsUsecase: GetCartDetailsUsecase,
    private val addToCartUsercase: AddToCartUsercase,
) :
    BaseViewModel() {
    val getProductDetailsResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<GetProductDetailsResponse>>>()

    var dashboardProduct = MutableLiveData<DashboardResponse.DashboardProduct>()

    val cartDetailsResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<GetCartDetailsResponse>>>()

    val addToCartResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<AddToCartResponse>>>()

    fun getProductDetails(id: String) {
        progressStatus.value = true
        viewModelScope.launch {
            var response = productDetailsUsecase(id)!!
            getProductDetailsResponseLiveData.value = response
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
}