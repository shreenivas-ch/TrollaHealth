package com.trolla.healthsdk.feature_auth.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trolla.healthsdk.ui_utils.LiveDataValidator
import com.trolla.healthsdk.ui_utils.LiveDataValidatorResolver
import com.trolla.healthsdk.utils.DateCalculator
import java.text.SimpleDateFormat
import java.util.*

class RegisterViewModel : ViewModel() {
    val firstNameLiveData = MutableLiveData<String>()
    val firstnameValidator = LiveDataValidator(firstNameLiveData).apply {
        addRule("First name is required") { it.isNullOrBlank() }
    }

    val lastnameLiveData = MutableLiveData<String>()
    val lastnameValidator = LiveDataValidator(lastnameLiveData).apply {
        addRule("Last Name is required") { it.isNullOrBlank() }
    }

    val mobileNumberLiveData = MutableLiveData<String>()
    val mobileNumberValidator = LiveDataValidator(mobileNumberLiveData).apply {
        addRule("Mobile number is required") { it.isNullOrBlank() }
        addRule("Mobile Number should be 10 digits") { it!!.length != 10 }
    }

    val dobDateLiveData = MutableLiveData<String>()
    val dobMonthLiveData = MutableLiveData<String>()
    val dobYearLiveData = MutableLiveData<String>()
    val dobLiveData = MutableLiveData<String>("")

    val dobValidator = LiveDataValidator(dobLiveData).apply {
        addRule("Age should be above 18") {
            var dateFormat = SimpleDateFormat("dd-MM-yyyy")
            var dateDOB = dateFormat.parse(dobLiveData.value.toString())
            val dateToday = Date()

            val startdate: Long = dateDOB.time
            val endDate: Long = dateToday.time

            var cal1 = Calendar.getInstance()
            cal1.timeInMillis = startdate

            var cal2 = Calendar.getInstance()
            cal2.timeInMillis = startdate

            if (startdate > endDate) {
                false
            } else {
                var age = DateCalculator.calculateAge(cal1, cal2)
                age.year < 18
            }
        }
    }

    val genderLiveData = MutableLiveData<String>()
    val genderValidator = LiveDataValidator(genderLiveData).apply {
        addRule("Please select your gender") { it != "" }
    }

    val registerFormValidMediator = MediatorLiveData<Boolean>()

    init {
        registerFormValidMediator.value = false
        registerFormValidMediator.addSource(firstNameLiveData) { validateForm() }
        registerFormValidMediator.addSource(lastnameLiveData) { validateForm() }
        registerFormValidMediator.addSource(mobileNumberLiveData) { validateForm() }
        registerFormValidMediator.addSource(genderLiveData) { validateForm() }
        registerFormValidMediator.addSource(dobDateLiveData) { validateForm() }
        registerFormValidMediator.addSource(dobMonthLiveData) { validateForm() }
        registerFormValidMediator.addSource(dobYearLiveData) { validateForm() }
    }

    private fun validateForm() {
        dobLiveData.value =
            dobDateLiveData.value.toString() + "-" + dobMonthLiveData.value.toString() + "-" + dobYearLiveData.value.toString()
        val validators =
            listOf(
                firstnameValidator,
                lastnameValidator,
                mobileNumberValidator,
                genderValidator,
                dobValidator
            )
        val validatorResolver = LiveDataValidatorResolver(validators)
        registerFormValidMediator.value = validatorResolver.isValid()
    }
}