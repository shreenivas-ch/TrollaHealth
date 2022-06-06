package com.trolla.healthsdk.feature_cart.presentation

import com.trolla.healthsdk.feature_cart.domain.usecases.AddToCartUsercase
import com.trolla.healthsdk.feature_cart.domain.usecases.GetCartDetailsUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel

class CartViewModel(
    private val addToCartUsercase: AddToCartUsercase,
    private val getCartDetailsUsecase: GetCartDetailsUsecase
) : BaseViewModel() {

}