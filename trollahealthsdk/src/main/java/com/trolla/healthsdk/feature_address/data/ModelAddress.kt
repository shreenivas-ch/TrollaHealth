package com.trolla.healthsdk.feature_address.data

import com.trolla.healthsdk.core.ListItemViewModel

data class ModelAddress(
    val _id: String = "",
    val name: String = "",
    val contact: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val landmark: String = "",
    val pincode: String = "",
    val type: String = "",

    /*local variables */
    val isSelect: Boolean

) : ListItemViewModel()
