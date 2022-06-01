package com.trolla.healthsdk.feature_productslist.data

import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.DashboardProduct

data class ProductsListResponse(
    val total_records: Int,
    val product_list: ArrayList<DashboardProduct>
)