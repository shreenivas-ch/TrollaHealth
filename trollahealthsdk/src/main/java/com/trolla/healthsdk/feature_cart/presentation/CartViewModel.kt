package com.trolla.healthsdk.feature_cart.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_cart.data.AddToCartResponse
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.domain.usecases.AddToCartUsercase
import com.trolla.healthsdk.feature_cart.domain.usecases.GetCartDetailsUsecase
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse
import com.trolla.healthsdk.ui_utils.BaseViewModel
import kotlinx.coroutines.launch

class CartViewModel(
    private val addToCartUsercase: AddToCartUsercase,
    private val getCartDetailsUsecase: GetCartDetailsUsecase
) : BaseViewModel() {

    val addToCartResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<AddToCartResponse>>>()

    val cartDetailsResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<GetCartDetailsResponse>>>()

    fun addToCart(product_id: String, qty: String) {
        progressStatus.value = true
        viewModelScope.launch {
            addToCartResponseLiveData.value =
                addToCartUsercase(product_id, qty)!!
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

}