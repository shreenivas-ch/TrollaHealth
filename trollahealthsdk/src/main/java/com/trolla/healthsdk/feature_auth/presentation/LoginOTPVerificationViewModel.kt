package com.trolla.healthsdk.feature_auth.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trolla.healthsdk.ui_utils.LiveDataValidator
import com.trolla.healthsdk.ui_utils.LiveDataValidatorResolver

class LoginOTPVerificationViewModel : ViewModel() {

    val otpLiveData = MutableLiveData<String>()
    val otpLiveData1 = MutableLiveData<String>()
    val otpLiveData2 = MutableLiveData<String>()
    val otpLiveData3 = MutableLiveData<String>()
    val otpLiveData4 = MutableLiveData<String>()

    val otpValidator = LiveDataValidator(otpLiveData).apply {
        addRule("Length should be 4 digits") { it.toString().length < 4 }
    }

    val otpValidator1 = LiveDataValidator(otpLiveData1).apply {
        addRule("Length should be 1 digit") { it.isNullOrBlank() }
    }

    val otpValidator2 = LiveDataValidator(otpLiveData2).apply {
        addRule("Length should be 1 digit") { it.isNullOrBlank() }
    }

    val otpValidator3 = LiveDataValidator(otpLiveData3).apply {
        addRule("Length should be 1 digit") { it.isNullOrBlank() }
    }

    val otpValidator4 = LiveDataValidator(otpLiveData4).apply {
        addRule("Length should be 1 digit") { it.isNullOrBlank() }
    }

    val formValidMediator = MediatorLiveData<Boolean>()

    init {
        formValidMediator.value = false
        formValidMediator.addSource(otpLiveData1) { validateForm() }
        formValidMediator.addSource(otpLiveData2) { validateForm() }
        formValidMediator.addSource(otpLiveData3) { validateForm() }
        formValidMediator.addSource(otpLiveData4) { validateForm() }
    }

    fun validateForm() {

        otpLiveData.value =
            otpLiveData1.value.toString() + otpLiveData2.value.toString() + otpLiveData3.value.toString() + otpLiveData4.value.toString()

        val validators =
            listOf(otpValidator, otpValidator1, otpValidator2, otpValidator3, otpValidator4)
        val validatorResolver = LiveDataValidatorResolver(validators)
        formValidMediator.value = validatorResolver.isValid()
    }

}