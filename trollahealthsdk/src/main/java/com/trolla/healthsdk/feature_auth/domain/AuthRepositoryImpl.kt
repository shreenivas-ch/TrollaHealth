package com.trolla.healthsdk.feature_auth.domain

import com.trolla.healthsdk.R
import com.trolla.healthsdk.feature_auth.data.AuthRepository
import com.trolla.healthsdk.feature_auth.data.models.LoginResponse
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.UiText
import com.trolla.healthsdk.data.models.BasicApiResponse
import com.trolla.healthsdk.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(private val apiService: ApiService) : AuthRepository {
    override suspend fun login(): Resource<BasicApiResponse<LoginResponse>> {
        return try {
            val response = apiService.authLogin()
            if (response.successful) {
                Resource.Success(response)
            } else {
                if (response.message != null) {
                    Resource.Error(uiText = UiText.DynamicString(response.message))
                } else {
                    Resource.Error(uiText = UiText.unknownerror())
                }
            }
        } catch (e: IOException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }
}

fun provideAuthRepository(apiService: ApiService): AuthRepository {
    return AuthRepositoryImpl(apiService)
}