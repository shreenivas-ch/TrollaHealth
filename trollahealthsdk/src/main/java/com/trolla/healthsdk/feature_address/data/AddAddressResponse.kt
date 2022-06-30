package com.trolla.healthsdk.feature_address.data

import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

class AddAddressResponse(
    val address:ModelAddress,
    val addresses: ArrayList<ModelAddress>
)