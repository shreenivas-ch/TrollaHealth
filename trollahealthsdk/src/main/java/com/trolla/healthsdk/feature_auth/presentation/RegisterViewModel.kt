package com.trolla.healthsdk.feature_auth.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_auth.domain.usecases.UpdateProfileUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.ui_utils.LiveDataValidator
import com.trolla.healthsdk.ui_utils.LiveDataValidatorResolver
import com.trolla.healthsdk.utils.DateCalculator
import com.trolla.healthsdk.utils.LogUtil
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterViewModel(private val updateProfileUsecase: UpdateProfileUsecase) : BaseViewModel() {

    val profileNameLiveData = MutableLiveData<String>()
    val profileEmailLiveData = MutableLiveData<String>()
    val profileMobileLiveData = MutableLiveData<String>()
    val profileIdLiveData = MutableLiveData<String>()
    val profileGenderLiveData = MutableLiveData<String>()
    val profileDOBLiveData = MutableLiveData<Date>()

    fun getProfile() {
        viewModelScope.launch {
            profileNameLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_NAME)
            profileEmailLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_EMAIL)
            profileMobileLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_MOBILE)
            profileIdLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_ID)
            profileGenderLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_GENDER)

            var day = TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_DAY)
            var month = TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_MONTH)
            var year = TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_YEAR)
            if (day.isNullOrEmpty() || month.isNullOrEmpty() || year.isNullOrEmpty()) {

            } else {
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year.toInt())
                cal.set(Calendar.MONTH, month.toInt() + 1)
                cal.set(Calendar.DAY_OF_MONTH, day.toInt())

                profileDOBLiveData.value = cal.time

                var d = day.toString()
                if (d.length == 1) {
                    d = "0$d"
                }

                var m = month.toString()
                if (m.length == 1) {
                    m = "0$m"
                }

                if (d != dobDateLiveData.value) {
                    dobDateLiveData.value = d
                }

                if (m != dobMonthLiveData.value) {
                    dobMonthLiveData.value = m
                }

                if (year.toString() != dobYearLiveData.value) {
                    dobYearLiveData.value = year.toString()
                }
            }
        }
    }

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
                firstNameLiveData.value?.trim() + " " + lastnameLiveData.value?.trim(),
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