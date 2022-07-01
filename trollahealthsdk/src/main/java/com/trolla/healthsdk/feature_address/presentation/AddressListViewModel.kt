package com.trolla.healthsdk.feature_address.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_address.data.*
import com.trolla.healthsdk.feature_address.domain.usecases.AddAddressUsecase
import com.trolla.healthsdk.feature_address.domain.usecases.DeleteAddressUsecase
import com.trolla.healthsdk.feature_address.domain.usecases.GetAddressListUsecase
import com.trolla.healthsdk.feature_address.domain.usecases.UpdateAddressUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import kotlinx.coroutines.launch

class AddressListViewModel(
    private val addAddressUsecase: AddAddressUsecase,
    private val deleteAddressUsecase: DeleteAddressUsecase,
    private val getAddressListUsecase: GetAddressListUsecase,
    private val updateAddressUsecase: UpdateAddressUsecase
) : BaseViewModel() {

    val getaddressListLiveData =
        MutableLiveData<Resource<BaseApiResponse<GetAdressListResponse>>>()

    val addAddressLiveData =
        MutableLiveData<Resource<BaseApiResponse<AddAddressResponse>>>()

    val deleteAddressLiveData = MutableLiveData<Resource<BaseApiResponse<DeleteAddressResponse>>>()

    val updateAddressLiveData = MutableLiveData<Resource<BaseApiResponse<EditAddressResponse>>>()

    fun addAddress(modelAddress: ModelAddress) {
        progressStatus.value = true
        viewModelScope.launch {
            addAddressLiveData.value =
                addAddressUsecase(modelAddress)!!
            progressStatus.value = false
        }
    }

    fun getAddressList() {
        progressStatus.value = true
        viewModelScope.launch {
            getaddressListLiveData.value =
                getAddressListUsecase()!!
            progressStatus.value = false
        }
    }

    fun updateAddress(id: String, modelAddress: ModelAddress) {
        progressStatus.value = true
        viewModelScope.launch {
            updateAddressLiveData.value =
                updateAddressUsecase(id, modelAddress)!!
            progressStatus.value = false
        }
    }

    fun deleteAddress(id: String) {
        progressStatus.value = true
        viewModelScope.launch {
            deleteAddressLiveData.value =
                deleteAddressUsecase(id)!!
            progressStatus.value = false
        }
    }

}