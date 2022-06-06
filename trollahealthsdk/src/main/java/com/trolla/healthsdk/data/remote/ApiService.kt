package com.trolla.healthsdk.data.remote

import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.data.models.CommonAPIResponse
import com.trolla.healthsdk.feature_auth.data.models.*
import com.trolla.healthsdk.feature_cart.data.AddToCartResponse
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_productdetails.data.GetProductDetailsResponse
import com.trolla.healthsdk.feature_productslist.data.ProductsListResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

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

    @GET("/products/list")
    suspend fun getProductsList(
    ): Response<BaseApiResponse<ProductsListResponse>>

    @GET("/products/details")
    suspend fun getProductDetails(
    ): Response<BaseApiResponse<GetProductDetailsResponse>>

    @GET("/")
    suspend fun getCartDetails(
    ): Response<BaseApiResponse<GetCartDetailsResponse>>

    @POST("/")
    suspend fun addToCart(
    ): Response<BaseApiResponse<AddToCartResponse>>


}