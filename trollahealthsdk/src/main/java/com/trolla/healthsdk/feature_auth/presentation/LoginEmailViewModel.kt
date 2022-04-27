package com.trolla.healthsdk.feature_auth.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.feature_auth.domain.usecases.LoginUseCase
import kotlinx.coroutines.launch

class LoginEmailViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    val email = MutableLiveData<String>()

    fun login() {
        viewModelScope.launch {

            var email = ""
            var password = ""
            when (val result = loginUseCase(email, password)) {
                is Resource.Success -> {
                    Log.e("EV----->", "Success")
                }
                is Resource.Error -> {

                }
            }
        }
    }
}