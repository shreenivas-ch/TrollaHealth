package com.trolla.healthsdk.feature_cart.data

class AddToCartResponse(
    val cart:AddToCartProducts
){
    data class AddToCartProducts(
        val products: GetCartDetailsResponse.CartProduct,
    )
}