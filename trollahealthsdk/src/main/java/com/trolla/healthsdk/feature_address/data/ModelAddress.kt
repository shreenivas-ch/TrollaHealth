package com.trolla.healthsdk.feature_address.data

import android.os.Parcelable
import com.trolla.healthsdk.core.ListItemViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelAddress(
    val _id: String = "",
    val name: String = "",
    val contact: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val landmark: String = "",
    val pincode: String = "",
    val type: String = "Other",
    val other: String = "",

    /*local variables */
    var isSelect: Boolean = false

) : ListItemViewModel(), Parcelable
