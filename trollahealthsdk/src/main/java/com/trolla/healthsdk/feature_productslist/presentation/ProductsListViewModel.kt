package com.trolla.healthsdk.feature_productslist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trolla.healthsdk.feature_productslist.data.ModelProduct

class ProductsListViewModel : ViewModel() {

    var items = MutableLiveData<List<ModelProduct>>()

}