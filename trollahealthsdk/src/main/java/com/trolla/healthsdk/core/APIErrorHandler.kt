package com.trolla.healthsdk.core

import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.UiText
import com.trolla.healthsdk.data.models.BaseApiResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class APIErrorHandler<T> {
    fun process(response: Response<BaseApiResponse<T>>): Resource<BaseApiResponse<T>> {
        return try {
            if (response.code() in 200..299) {
                Resource.Success(
                    response.body(),
                    UiText.DynamicString(response.body()?.message ?: "")
                )
            } else {
                when {
                    response.body()?.message != null -> {
                        Resource.Error(uiText = UiText.DynamicString("Server Error: " + response.body()?.message + "\n" + "Code:" + response.code()))
                    }
                    response.message() != null -> {
                        Resource.Error(uiText = UiText.DynamicString("Server Error: " + response.message() + "\n" + "Code:" + response.code()))
                    }
                    else -> {
                        Resource.Error(uiText = UiText.DynamicString("Server Error\nCode:" + response.code()))
                    }
                }
            }
        }
        catch (e:Exception)
        {
            when (e) {
                is IOException -> {
                    Resource.Error(
                        uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
                    )
                }
                is HttpException -> {
                    Resource.Error(
                        uiText = UiText.StringResource(R.string.oops_something_went_wrong)
                    )
                }
                is SocketTimeoutException -> {
                    Resource.Error(
                        uiText = UiText.StringResource(R.string.server_not_reachable)
                    )
                }
                else -> {
                    Resource.Error(
                        uiText = UiText.StringResource(R.string.error_couldnt_reach_server)
                    )
                }
            }
        }
    }
}