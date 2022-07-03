package com.trolla.healthsdk.feature_address.domain

import com.trolla.healthsdk.core.APIErrorHandler
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_address.data.*
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productdetails.data.ProductDetailsRepository

class AddressRepositoryImpl(private val apiService: ApiService) : AddressRepository {

    override suspend fun getAddressList(): Resource<BaseApiResponse<GetAdressListResponse>> {
        val response = apiService.getAddressList()
        return APIErrorHandler<GetAdressListResponse>().process(response)
    }

    override suspend fun updateAddress(
        id: String,
        modelAddress: AddAddressRequest
    ): Resource<BaseApiResponse<EditAddressResponse>> {
        val response = apiService.updateAddress(id, modelAddress)
        return APIErrorHandler<EditAddressResponse>().process(response)
    }

    override suspend fun addAddress(modelAddress: AddAddressRequest): Resource<BaseApiResponse<AddAddressResponse>> {
        val response = apiService.addAddress(modelAddress)
        return APIErrorHandler<AddAddressResponse>().process(response)
    }

    override suspend fun deleteAddress(id: String): Resource<BaseApiResponse<DeleteAddressResponse>> {
        val response = apiService.deleteAddress(id)
        return APIErrorHandler<DeleteAddressResponse>().process(response)
    }
}

fun provideAddressRepository(apiService: ApiService): AddressRepository {
    return AddressRepositoryImpl(apiService)
}