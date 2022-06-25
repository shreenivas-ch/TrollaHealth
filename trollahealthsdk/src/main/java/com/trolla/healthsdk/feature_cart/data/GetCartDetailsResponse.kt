package com.trolla.healthsdk.feature_cart.data

import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse

class GetCartDetailsResponse(
    val products: CartProduct,
) {
    data class CartProduct(
        val product: DashboardResponse.DashboardProduct,
        val qty: Int
    )
}