package com.trolla.healthsdk.feature_productdetails.data

import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

class GetProductDetailsResponse(
    val detail: DashboardResponse.DashboardProduct,
    val variants:ArrayList<DashboardResponse.ProductVariant>,
)