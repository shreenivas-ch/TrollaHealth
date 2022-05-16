package com.trolla.healthsdk.feature_auth.presentation

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.feature_auth.domain.usecases.GetOTPOnEmailUsecase
import com.trolla.healthsdk.ui_utils.LiveDataValidator
import com.trolla.healthsdk.ui_utils.LiveDataValidatorResolver
import kotlinx.coroutines.launch

class LoginEmailViewModel(private val loginUseCase: GetOTPOnEmailUsecase) : ViewModel() {

    val emailLiveData = MutableLiveData<String>()
    val emailValidator = LiveDataValidator(emailLiveData).apply {
        //Whenever the condition of the predicate is true, the error message should be emitted
        addRule("Email is required") { it.isNullOrBlank() }
        addRule("Email is not valid") {
            !Patterns.EMAIL_ADDRESS.matcher(emailLiveData.value).matches()
        }
    }

    val isLoginFormValidMediator = MediatorLiveData<Boolean>()

    init {
        isLoginFormValidMediator.value = false
        isLoginFormValidMediator.addSource(emailLiveData) { validateForm() }
    }

    //This is called whenever the usernameLiveData and passwordLiveData changes
    fun validateForm() {
        val validators = listOf(emailValidator)
        val validatorResolver = LiveDataValidatorResolver(validators)
        isLoginFormValidMediator.value = validatorResolver.isValid()
    }

    /*val emailErrorLiveData = MutableLiveData<String>()
    val isLoginFormValidLiveData = MutableLiveData<Boolean>()*/

    /*fun validateForm() {
        isLoginFormValidLiveData.value = false
        if (emailLiveData.value.isNullOrEmpty()) {
            emailErrorLiveData.value = "Email ID is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailLiveData.value).matches()) {
            emailErrorLiveData.value = "Email ID is Not Valid"
        } else {
            emailErrorLiveData.value = ""
            isLoginFormValidLiveData.value = true
        }
    }*/

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