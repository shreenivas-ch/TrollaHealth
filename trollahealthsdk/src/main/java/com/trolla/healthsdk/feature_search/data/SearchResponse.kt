package com.trolla.healthsdk.feature_search.data

import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

data class SearchResponse(
    val product_list_1: ArrayList<DashboardResponse.DashboardProduct>,
    val product_list: ArrayList<DashboardResponse.DashboardProduct>
)