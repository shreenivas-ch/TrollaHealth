package com.trolla.healthsdk.feature_address.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse

interface AddressRepository {
    suspend fun getAddressList(): Resource<BaseApiResponse<GetAdressListResponse>>
    suspend fun updateAddress(id:String,modelAddress: ModelAddress): Resource<BaseApiResponse<EditAddressResponse>>
    suspend fun addAddress(modelAddress: ModelAddress): Resource<BaseApiResponse<AddAddressResponse>>
    suspend fun deleteAddress(id:String): Resource<BaseApiResponse<DeleteAddressResponse>>
}