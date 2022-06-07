package com.trolla.healthsdk.di

import com.trolla.healthsdk.feature_auth.domain.provideAuthRepository
import com.trolla.healthsdk.data.remote.RetrofitFactory
import com.trolla.healthsdk.feature_auth.domain.usecases.GetOTPOnEmailUsecase
import com.trolla.healthsdk.feature_auth.domain.usecases.UpdateProfileUsecase
import com.trolla.healthsdk.feature_auth.domain.usecases.VerifyOTPOnMobileUsecase
import com.trolla.healthsdk.feature_auth.domain.usecases.VerifyOTPOnEmailUsecase
import com.trolla.healthsdk.feature_auth.presentation.LoginEmailViewModel
import com.trolla.healthsdk.feature_auth.presentation.LoginOTPVerificationViewModel
import com.trolla.healthsdk.feature_auth.presentation.MobileOTPVerificationViewModel
import com.trolla.healthsdk.feature_auth.presentation.RegisterViewModel
import com.trolla.healthsdk.feature_cart.domain.provideCartRepository
import com.trolla.healthsdk.feature_cart.domain.usecases.AddToCartUsercase
import com.trolla.healthsdk.feature_cart.domain.usecases.GetCartDetailsUsecase
import com.trolla.healthsdk.feature_cart.presentation.CartViewModel
import com.trolla.healthsdk.feature_dashboard.domain.provideDashboardRepository
import com.trolla.healthsdk.feature_dashboard.domain.usecases.GetDashboardUsecase
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardViewModel
import com.trolla.healthsdk.feature_productdetails.domain.provideProductDetailsRepository
import com.trolla.healthsdk.feature_productdetails.domain.usecases.GetProductDetailsUsecase
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsViewModel
import com.trolla.healthsdk.feature_productslist.domain.provideProductsListRepository
import com.trolla.healthsdk.feature_productslist.domain.usecases.GetProductsListUsecase
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListViewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { RetrofitFactory.makeRetrofitService() }
    single { provideAuthRepository(get()) }

    single { GetOTPOnEmailUsecase(get()) }
    single { LoginEmailViewModel(get()) }

    single { VerifyOTPOnEmailUsecase(get()) }
    single { VerifyOTPOnMobileUsecase(get()) }
    single { LoginOTPVerificationViewModel(get()) }
    single { MobileOTPVerificationViewModel(get()) }

    single { UpdateProfileUsecase(get()) }
    single { RegisterViewModel(get()) }

    single { provideDashboardRepository(get()) }
    single { GetDashboardUsecase(get()) }
    single { DashboardViewModel(get()) }

    single { provideProductsListRepository(get()) }
    single { GetProductsListUsecase(get()) }
    single { ProductsListViewModel(get()) }

    single { provideProductDetailsRepository(get()) }
    single { GetProductDetailsUsecase(get()) }
    single { ProductDetailsViewModel(get()) }

    single { provideCartRepository(get()) }
    single { GetCartDetailsUsecase(get()) }
    single { AddToCartUsercase(get()) }
    single { CartViewModel(get(), get()) }
}