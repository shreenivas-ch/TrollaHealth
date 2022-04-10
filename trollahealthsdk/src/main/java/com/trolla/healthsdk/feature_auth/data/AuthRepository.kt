package com.trolla.healthsdk.feature_auth.data

import com.trolla.healthsdk.feature_auth.data.models.LoginResponse
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BasicApiResponse

interface AuthRepository {
    suspend fun login(): Resource<BasicApiResponse<LoginResponse>>
}