package com.trolla.healthsdk.feature_address.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_address.data.AddAddressRequest
import com.trolla.healthsdk.feature_address.data.AddAddressResponse
import com.trolla.healthsdk.feature_address.data.EditAddressResponse
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_address.domain.usecases.AddAddressUsecase
import com.trolla.healthsdk.feature_address.domain.usecases.UpdateAddressUsecase
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.ui_utils.LiveDataValidator
import com.trolla.healthsdk.ui_utils.LiveDataValidatorResolver
import com.trolla.healthsdk.utils.LogUtil
import kotlinx.coroutines.launch

class AddAddressViewModel(
    private val addAddressUsecase: AddAddressUsecase,
    private val updateAddressUsecase: UpdateAddressUsecase
) : BaseViewModel() {

    val addressIdLiveData =
        MutableLiveData<String>("")

    val addAddressLiveData =
        MutableLiveData<Resource<BaseApiResponse<AddAddressResponse>>>()

    val updateAddressLiveData = MutableLiveData<Resource<BaseApiResponse<EditAddressResponse>>>()

    val addressLiveData = MutableLiveData<String>()
    val addressValidator = LiveDataValidator(addressLiveData).apply {
        addRule("Enter address") { it.isNullOrBlank() }
    }

    val addressNameLiveData = MutableLiveData<String>()
    val addressNameValidator = LiveDataValidator(addressNameLiveData).apply {
        addRule("Enter contact name") { it.isNullOrBlank() }
    }

    val addressContactLiveData = MutableLiveData<String>()
    val addressContactValidator = LiveDataValidator(addressContactLiveData).apply {
        addRule("Enter 10 digit contact number") { it.isNullOrBlank() }
        addRule("Enter 10 digit mobile number") { it?.length != 10 }
    }

    val addressLandmarkLiveData = MutableLiveData<String>()
    val addressLandmarkValidator = LiveDataValidator(addressLandmarkLiveData).apply {
        addRule("Enter landmark") { it.isNullOrBlank() }
    }

    val addressCityLiveData = MutableLiveData<String>()
    val addressCityValidator = LiveDataValidator(addressCityLiveData).apply {
        addRule("Enter city") { it.isNullOrBlank() }
    }

    val addressStateLiveData = MutableLiveData<String>()
    val addressStateValidator = LiveDataValidator(addressStateLiveData).apply {
        addRule("Enter state") { it.isNullOrBlank() }
    }

    val addressPincodeLiveData = MutableLiveData<String>()
    val addressPincodeValidator = LiveDataValidator(addressPincodeLiveData).apply {
        addRule("Enter pincode") { it.isNullOrBlank() }
        addRule("Enter valid pincode") { it?.length != 6 }
    }


    var addressTypeHomeConstant = "home"
    var addressTypeWorkConstant = "work"
    var addressTypeOtherConstant = "other"
    val addressTypeLiveData = MutableLiveData<String>("")
    val addressTypeValidator = LiveDataValidator(addressTypeLiveData).apply {
        addRule("Please select save address as") { it == "" }
    }

    val addressOtheriveData = MutableLiveData<String>("")
    val addressOtherValidator = LiveDataValidator(addressOtheriveData).apply {
        addRule("Enter name for this location") {
            addressTypeLiveData.value == "other" && it.isNullOrBlank()
        }
    }

    val addAddressFormValidMediator = MediatorLiveData<Boolean>()

    init {
        addAddressFormValidMediator.value = false
        addAddressFormValidMediator.addSource(addressNameLiveData) { validateForm() }
        addAddressFormValidMediator.addSource(addressContactLiveData) { validateForm() }
        addAddressFormValidMediator.addSource(addressLiveData) { validateForm() }
        addAddressFormValidMediator.addSource(addressTypeLiveData) { validateForm() }
        addAddressFormValidMediator.addSource(addressCityLiveData) { validateForm() }
        addAddressFormValidMediator.addSource(addressStateLiveData) { validateForm() }
        addAddressFormValidMediator.addSource(addressPincodeLiveData) { validateForm() }
        addAddressFormValidMediator.addSource(addressLandmarkLiveData) { validateForm() }
        addAddressFormValidMediator.addSource(addressOtheriveData) { validateForm() }
    }

    private fun validateForm() {

        val validators =
            listOf(
                addressNameValidator,
                addressContactValidator,
                addressValidator,
                addressLandmarkValidator,
                addressCityValidator,
                addressStateValidator,
                addressPincodeValidator,
                addressTypeValidator,
                addressOtherValidator
            )
        val validatorResolver = LiveDataValidatorResolver(validators)
        addAddressFormValidMediator.value = validatorResolver.isValid()
    }

    fun saveAddress() {

        var _id = addressIdLiveData.value
        var name = addressNameLiveData.value
        var contact = addressContactLiveData.value?.toLong()
        var address = addressLiveData.value
        var city = addressCityLiveData.value
        var state = addressStateLiveData.value
        var landmark = addressLandmarkLiveData.value
        var pincode = addressPincodeLiveData.value?.toLong()
        var addressType = addressTypeLiveData.value
        var otherAddress = addressOtheriveData.value

        var modelAddress = AddAddressRequest(_id!!,name!!,contact!!,address!!,city!!,state!!,landmark!!,pincode!!,addressType!!,otherAddress!!)
        progressStatus.value = true
        if (addressIdLiveData.value == "") {
            viewModelScope.launch {
                addAddressLiveData.value =
                    addAddressUsecase(modelAddress)!!
                progressStatus.value = false
            }
        } else {
            viewModelScope.launch {
                updateAddressLiveData.value =
                    updateAddressUsecase(addressIdLiveData.value ?: "", modelAddress)!!
                progressStatus.value = false

            }

        }
    }

}