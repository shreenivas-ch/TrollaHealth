package com.trolla.healthsdk.data.remote

import com.trolla.healthsdk.feature_auth.data.models.LoginResponse
import com.trolla.healthsdk.data.models.BasicApiResponse
import retrofit2.http.GET

interface ApiService {

    @GET("/auth/login")
    suspend fun authLogin(): BasicApiResponse<LoginResponse>

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}