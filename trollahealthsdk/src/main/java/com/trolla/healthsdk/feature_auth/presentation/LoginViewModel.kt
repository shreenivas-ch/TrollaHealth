package com.trolla.healthsdk.feature_auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.feature_auth.domain.usecases.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    fun login() {
        viewModelScope.launch {

            when (val result = loginUseCase()) {
                is Resource.Success -> {
                    Log.e("EV----->", "Success")
                }
                is Resource.Error -> {

                }
            }
        }
    }
}