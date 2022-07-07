package com.trolla.healthsdk.feature_cart.data

import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_prescriptionupload.data.ModelPrescription

class AddToCartResponse(
    val cart: AddToCartProducts
) {
    data class AddToCartProducts(
        val products: ArrayList<GetCartDetailsResponse.CartProduct>,
        val prescriptions:ArrayList<String>
    )
}