package com.trolla.healthsdk.core

import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.UiText
import com.trolla.healthsdk.data.models.BaseApiResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class APIErrorHandler<T> {
    fun process(response: Response<BaseApiResponse<T>>): Resource<BaseApiResponse<T>> {
        return try {
            if (response.code() in 200..299) {
                Resource.Success(response.body())
            } else {
                when {
                    response.body()?.message != null -> {
                        Resource.Error(uiText = UiText.DynamicString(response.body()?.message ?: ""))
                    }
                    response.message() != null -> {
                        Resource.Error(uiText = UiText.DynamicString(response.message()))
                    }
                    else -> {
                        Resource.Error(uiText = UiText.unknownerror())
                    }
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