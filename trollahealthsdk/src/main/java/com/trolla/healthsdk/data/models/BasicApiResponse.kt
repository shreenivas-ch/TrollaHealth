package com.trolla.healthsdk.data.models

data class BasicApiResponse<T>(
    val successful: Boolean? = true,
    val message: String? = null,
    val data: T? = null
)
