package com.trolla.healthsdk.feature_productslist.data

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

interface ProductsListRepository {
    suspend fun getDashboard(): Resource<BaseApiResponse<DashboardResponse>>
}