package com.trolla.healthsdk.feature_productdetails.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productdetails.data.ProductDetailsRepository
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse

class GetProductDetailsUsecase(private val productDetailsRepository: ProductDetailsRepository) {
    suspend operator fun invoke(
    ): Resource<BaseApiResponse<GetProductDetailsResponse>> {
        return productDetailsRepository.getProductDetails()
    }
}