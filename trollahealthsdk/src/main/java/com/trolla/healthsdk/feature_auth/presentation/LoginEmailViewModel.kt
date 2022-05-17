package com.trolla.healthsdk.feature_auth.presentation

import android.util.Patterns
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.domain.usecases.GetOTPOnEmailUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.ui_utils.LiveDataValidator
import com.trolla.healthsdk.ui_utils.LiveDataValidatorResolver
import com.trolla.healthsdk.utils.LogUtil
import kotlinx.coroutines.launch

class LoginEmailViewModel(private val loginUseCase: GetOTPOnEmailUsecase) : BaseViewModel() {

    val getOTPResponse = MutableLiveData<Resource<BaseApiResponse<CommonAPIResponse>>>()
    val emailLiveData = MutableLiveData<String>()
    val emailValidator = LiveDataValidator(emailLiveData).apply {
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

    fun validateForm() {
        val validators = listOf(emailValidator)
        val validatorResolver = LiveDataValidatorResolver(validators)
        isLoginFormValidMediator.value = validatorResolver.isValid()
    }

    fun login() {
        progressStatus.value = true
        viewModelScope.launch {
            getOTPResponse.value = loginUseCase(emailLiveData.value.toString(), "")!!
            progressStatus.value = false
            LogUtil.printObject(getOTPResponse.value.toString())
        }
    }
}