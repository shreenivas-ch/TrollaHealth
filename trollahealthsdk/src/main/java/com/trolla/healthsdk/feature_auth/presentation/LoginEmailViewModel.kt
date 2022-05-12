package com.trolla.healthsdk.feature_auth.presentation

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.feature_auth.domain.usecases.GetOTPOnEmailUsecase
import com.trolla.healthsdk.utils.LiveDataValidator
import com.trolla.healthsdk.utils.LiveDataValidatorResolver
import kotlinx.coroutines.launch

class LoginEmailViewModel(private val loginUseCase: GetOTPOnEmailUsecase) : ViewModel() {

    val emailLiveData = MutableLiveData<String>()
    val emailValidator = LiveDataValidator(emailLiveData).apply {
        //Whenever the condition of the predicate is true, the error message should be emitted
        addRule("Email ID is required") { it.isNullOrBlank() }
        addRule("Email ID is Not Valid") {
            Patterns.EMAIL_ADDRESS.matcher(it).matches()
        }
    }

    val isLoginFormValidMediator = MediatorLiveData<Boolean>()

    init {
        isLoginFormValidMediator.value = true
        isLoginFormValidMediator.addSource(emailLiveData) { validateForm() }
    }

    fun validateForm() {
        val validators = listOf(emailValidator)
        val validatorResolver = LiveDataValidatorResolver(validators)
        isLoginFormValidMediator.value = validatorResolver.isValid()
    }

    fun login() {
        viewModelScope.launch {

            when (val result = loginUseCase(emailLiveData.value.toString(), "")) {
                is Resource.Success -> {
                    Log.e("EV----->", "Success")
                }
                is Resource.Error -> {

                }
            }
        }
    }
}