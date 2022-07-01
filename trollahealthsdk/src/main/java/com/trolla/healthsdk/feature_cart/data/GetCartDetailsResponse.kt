package com.trolla.healthsdk.feature_cart.data

import com.trolla.healthsdk.core.ListItemViewModel
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_prescriptionupload.data.ModelPrescription

class GetCartDetailsResponse(
    val products: ArrayList<CartProduct>,
    val address: ModelAddress,
    val prescriptions: ArrayList<ModelPrescription>
) {
    data class CartProduct(
        val product: DashboardResponse.DashboardProduct,
        val qty: Int
    ) : ListItemViewModel()
}