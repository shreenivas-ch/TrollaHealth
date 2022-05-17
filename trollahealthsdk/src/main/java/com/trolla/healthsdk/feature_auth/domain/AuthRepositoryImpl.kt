package com.trolla.healthsdk.feature_auth.domain

import com.trolla.healthsdk.R
import com.trolla.healthsdk.feature_auth.data.AuthRepository
import com.trolla.healthsdk.feature_auth.data.models.GetOTPResponse
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.UiText
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.data.remote.ApiService
import com.trolla.healthsdk.feature_auth.data.models.GetOTPRequest
import com.trolla.healthsdk.utils.LogUtil
import retrofit2.HttpException
import java.io.IOException

class AuthRepositoryImpl(private val apiService: ApiService) : AuthRepository {
    override suspend fun getOTP(
        email: String,
        mobile: String
    ): Resource<BaseApiResponse<CommonAPIResponse>> {
        return try {
            val response = apiService.authGetOTP(GetOTPRequest(email))
            if (response.code() == 200) {
                Resource.Success(response.body())
            } else {
                if (response.body()?.message != null) {
                    Resource.Error(uiText = UiText.DynamicString(response.message))
                } else {
                    Resource.Error(uiText = UiText.unknownerror())
                }
            }
        } catch (e: IOException) {
            LogUtil.printObject(e.toString())
            Resource.Error(
                uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
            )
        } catch (e: HttpException) {
            Resource.Error(
                uiText = UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    }

    override suspend fun verifyOTP(
        email: String,
        mobile: String,
        otp: String
    ): Unit {

    }
}

fun provideAuthRepository(apiService: ApiService): AuthRepository {
    return AuthRepositoryImpl(apiService)
}