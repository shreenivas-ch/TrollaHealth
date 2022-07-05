package com.trolla.healthsdk.feature_orders.data

import com.trolla.healthsdk.core.ListItemViewModel
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse

data class ModelOrderProductImage(
    val img_url: String,
) : ListItemViewModel()