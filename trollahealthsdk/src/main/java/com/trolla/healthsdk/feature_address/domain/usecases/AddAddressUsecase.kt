package com.trolla.healthsdk.feature_address.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_address.data.AddAddressResponse
import com.trolla.healthsdk.feature_address.data.AddressRepository
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productdetails.data.ProductDetailsRepository
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse

class AddAddressUsecase(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(
        modelAddress: ModelAddress
    ): Resource<BaseApiResponse<AddAddressResponse>> {
        return addressRepository.addAddress(modelAddress)
    }
}