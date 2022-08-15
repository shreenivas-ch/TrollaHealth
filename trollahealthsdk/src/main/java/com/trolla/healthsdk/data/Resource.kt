package com.trolla.healthsdk.data

sealed class Resource<T>(val data: T? = null, val uiText: UiText? = null) {
    class Success<T>(data: T?, uiText: UiText? = null) : Resource<T>(data, uiText)
    class Error<T>(uiText: UiText?, data: T? = null) : Resource<T>(data, uiText)
}
