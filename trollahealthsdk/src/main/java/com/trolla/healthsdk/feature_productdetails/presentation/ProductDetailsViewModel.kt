package com.trolla.healthsdk.feature_productdetails.presentation

import com.trolla.healthsdk.feature_productdetails.domain.usecases.GetProductDetailsUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel

class ProductDetailsViewModel(private val productDetailsUsecase: GetProductDetailsUsecase) :
    BaseViewModel() {

}