package com.trolla.healthsdk.data.models

data class BaseApiResponse<T : Any?>(
    val successful: Boolean? = true,
    val message: String? = null,
    val data: T? = null
)
