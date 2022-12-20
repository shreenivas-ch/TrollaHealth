package com.trolla.healthsdk.feature_categories.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_categories.data.CategoriesResponse
import com.trolla.healthsdk.feature_categories.domain.usecases.GetCategoriesUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import kotlinx.coroutines.launch

class CategoriesViewModel(private val getCategoriesUsecase: GetCategoriesUsecase) :
    BaseViewModel() {

    val categoriesResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<CategoriesResponse>>>()

    fun getCategories() {
        progressStatus.value = true
        viewModelScope.launch {
            categoriesResponseLiveData.value = getCategoriesUsecase()!!
            progressStatus.value = false
        }
    }
}