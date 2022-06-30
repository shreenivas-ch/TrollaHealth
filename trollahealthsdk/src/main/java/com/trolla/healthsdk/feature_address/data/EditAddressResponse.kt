package com.trolla.healthsdk.feature_address.data

import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

class EditAddressResponse(
    val address:ModelAddress,
    val addresses: ArrayList<ModelAddress>
)