package com.trolla.healthsdk.feature_dashboard.data

data class DashboardComponentModel<T>(
    val template: String,
    val data: T
)