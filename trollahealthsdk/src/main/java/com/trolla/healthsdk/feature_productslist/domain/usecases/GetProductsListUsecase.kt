package com.trolla.healthsdk.feature_productslist.domain.usecases

import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_productslist.data.ProductsListRepository
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse

class GetProductsListUsecase(private val productsListRepository: ProductsListRepository) {
    suspend operator fun invoke(
        page: String, limit: String, filterValue: String, filterBy: String
    ): Resource<BaseApiResponse<ProductsListResponse>> {
        return productsListRepository.getProductsList(page, limit, filterValue, filterBy)
    }
}