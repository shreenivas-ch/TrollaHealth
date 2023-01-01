package com.trolla.healthsdk.core

import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.UiText
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.TrollaLogoutEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class APIErrorHandler<T> {
    fun process(response: Response<BaseApiResponse<T>>): Resource<BaseApiResponse<T>> {
        return try {
            var code = response.code()
            when (code) {
                in 200..299 -> {
                    Resource.Success(
                        response.body(),
                        UiText.DynamicString(response.body()?.message ?: "")
                    )
                }
                in 400..499 -> {
                    EventBus.getDefault()
                        .post(TrollaLogoutEvent("Authentication failed, please try to login again\nCode:$code"))
                    Resource.Error(uiText = UiText.DynamicString("auth_failed"))
                }
                else -> {
                    when {
                        response.body()?.message != null -> {
                            Resource.Error(uiText = UiText.DynamicString("Server Error: " + response.body()?.message + "\n" + "Code:" + code))
                        }
                        response.message() != null -> {
                            Resource.Error(uiText = UiText.DynamicString("Server Error: " + response.message() + "\n" + "Code:" + code))
                        }
                        else -> {
                            Resource.Error(uiText = UiText.DynamicString("Server Error\nCode:$code"))
                        }
                    }
                }
            }
        } catch (e: Exception) {
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