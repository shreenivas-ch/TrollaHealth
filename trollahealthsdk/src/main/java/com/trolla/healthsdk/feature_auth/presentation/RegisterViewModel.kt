package com.trolla.healthsdk.feature_auth.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_auth.domain.usecases.UpdateProfileUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.ui_utils.LiveDataValidator
import com.trolla.healthsdk.ui_utils.LiveDataValidatorResolver
import com.trolla.healthsdk.utils.DateCalculator
import com.trolla.healthsdk.utils.LogUtil
import kotlinx.coroutines.launch
import org.koin.core.component.getScopeId
import java.text.SimpleDateFormat
import java.util.*

class RegisterViewModel(private val updateProfileUsecase: UpdateProfileUsecase) : BaseViewModel() {

    val updateProfileResponse = MutableLiveData<Resource<BaseApiResponse<UpdateProfileResponse>>>()

    var genderMaleConstant = "male"
    var genderFemaleConstant = "female"
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
            cal2.timeInMillis = endDate

            var age = DateCalculator.calculateAge(cal1, cal2)

            LogUtil.printObject("----->" + age.year)

            age.year < 18 || startdate > endDate

        }
    }

    val genderLiveData = MutableLiveData<String>("")
    val genderValidator = LiveDataValidator(genderLiveData).apply {
        addRule("Please select your gender") { it == "" }
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
        //registerFormValidMediator.addSource(dobLiveData) { validateForm() }
    }

    private fun validateForm() {
        dobLiveData.value =
            dobDateLiveData.value.toString() + "-" + dobMonthLiveData.value.toString() + "-" + dobYearLiveData.value.toString()

        LogUtil.printObject("----->1: " + dobLiveData.value.toString())

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

    fun updateProfile() {
        progressStatus.value = true
        viewModelScope.launch {
            updateProfileResponse.value = updateProfileUsecase(
                firstNameLiveData.value + " " + lastnameLiveData.value,
                mobileNumberLiveData.value.toString(), genderLiveData.value.toString(),
                dobDateLiveData.value.toString(),
                dobMonthLiveData.value.toString(),
                dobYearLiveData.value.toString()
            )!!
            progressStatus.value = false
            LogUtil.printObject(updateProfileResponse.value.toString())
        }
    }
}