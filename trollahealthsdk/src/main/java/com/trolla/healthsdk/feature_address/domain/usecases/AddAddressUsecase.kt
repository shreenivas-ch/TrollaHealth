package com.trolla.healthsdk.feature_address.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_address.data.AddAddressRequest
import com.trolla.healthsdk.feature_address.data.AddAddressResponse
import com.trolla.healthsdk.feature_address.data.AddressRepository

class AddAddressUsecase(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(
        modelAddress: AddAddressRequest
    ): Resource<BaseApiResponse<AddAddressResponse>> {
        return addressRepository.addAddress(modelAddress)
    }
}