package com.trolla.healthsdk.feature_cart.data

import com.trolla.healthsdk.core.ListItemViewModel
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

class GetCartDetailsResponse(
    val products: List<CartProduct>,
) {
    data class CartProduct(
        val product: DashboardResponse.DashboardProduct,
        val qty: Int
    ): ListItemViewModel()
}