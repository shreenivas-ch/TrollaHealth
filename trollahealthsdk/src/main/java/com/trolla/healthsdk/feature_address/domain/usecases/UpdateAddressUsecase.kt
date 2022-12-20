package com.trolla.healthsdk.feature_address.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_address.data.AddAddressRequest
import com.trolla.healthsdk.feature_address.data.AddressRepository
import com.trolla.healthsdk.feature_address.data.EditAddressResponse

class UpdateAddressUsecase(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(
        id: String, modelAddress: AddAddressRequest
    ): Resource<BaseApiResponse<EditAddressResponse>> {
        return addressRepository.updateAddress(id, modelAddress)
    }
}