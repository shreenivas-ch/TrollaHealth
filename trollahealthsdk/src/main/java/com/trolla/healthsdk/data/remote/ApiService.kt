package com.trolla.healthsdk.data.remote

import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.data.models.*
import com.trolla.healthsdk.feature_cart.data.AddToCartResponse
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.data.models.AddToCartRequest
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("/auth/get-otp")
    suspend fun authGetOTP(
        @Body request: GetOTPRequest
    ): Response<BaseApiResponse<CommonAPIResponse>>

    @POST("/auth/verify-otp")
    suspend fun authVerifyOTP(
        @Body request: VerifyOTPRequest
    ): Response<BaseApiResponse<VerifyOTPResponse>>

    @PUT("/users/profile")
    suspend fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest): Response<BaseApiResponse<UpdateProfileResponse>>

    @GET("/")
    suspend fun getDashboard(
    ): Response<BaseApiResponse<DashboardResponse>>

    @GET("/products")
    suspend fun getProductsList(
        @Query("page") page: String,
        @Query("limit") limit: String,
        @Query("category") category: String,
        @Query("type") type: String,
    ): Response<BaseApiResponse<ProductsListResponse>>

    @GET("/products/{id}")
    suspend fun getProductDetails(
        @Path("id") id: String
    ): Response<BaseApiResponse<GetProductDetailsResponse>>

    @GET("/cart")
    suspend fun getCartDetails(
    ): Response<BaseApiResponse<GetCartDetailsResponse>>

    @POST("/cart/add")
    suspend fun addToCart(
        @Body request: AddToCartRequest
    ): Response<BaseApiResponse<AddToCartResponse>>
}