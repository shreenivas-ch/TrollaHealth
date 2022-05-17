package com.trolla.healthsdk.ui_utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel:ViewModel() {
    val progressStatus = MutableLiveData<Boolean>()
}