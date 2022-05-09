package com.trolla.healthsdk.feature_auth.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.feature_auth.domain.usecases.GetOTPOnEmailUsecase
import kotlinx.coroutines.launch

class LoginEmailViewModel(private val loginUseCase: GetOTPOnEmailUsecase) : ViewModel() {

    val email = MutableLiveData<String>()

    fun login() {
        viewModelScope.launch {

            when (val result = loginUseCase(email.value.toString(),"")) {
                is Resource.Success -> {
                    Log.e("EV----->", "Success")
                }
                is Resource.Error -> {

                }
            }
        }
    }
}