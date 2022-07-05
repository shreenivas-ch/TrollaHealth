package com.trolla.healthsdk.di

import com.trolla.healthsdk.feature_auth.domain.provideAuthRepository
import com.trolla.healthsdk.data.remote.RetrofitFactory
import com.trolla.healthsdk.feature_address.domain.provideAddressRepository
import com.trolla.healthsdk.feature_address.domain.usecases.AddAddressUsecase
import com.trolla.healthsdk.feature_address.domain.usecases.DeleteAddressUsecase
import com.trolla.healthsdk.feature_address.domain.usecases.GetAddressListUsecase
import com.trolla.healthsdk.feature_address.domain.usecases.UpdateAddressUsecase
import com.trolla.healthsdk.feature_address.presentation.AddAddressViewModel
import com.trolla.healthsdk.feature_address.presentation.AddressListViewModel
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
import com.trolla.healthsdk.feature_cart.domain.usecases.CreateOrderUsecase
import com.trolla.healthsdk.feature_cart.domain.usecases.GetCartDetailsUsecase
import com.trolla.healthsdk.feature_cart.presentation.CartViewModel
import com.trolla.healthsdk.feature_dashboard.domain.provideDashboardRepository
import com.trolla.healthsdk.feature_dashboard.domain.usecases.GetDashboardUsecase
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardViewModel
import com.trolla.healthsdk.feature_dashboard.presentation.ProfileViewModel
import com.trolla.healthsdk.feature_dashboard.presentation.SettingsViewModel
import com.trolla.healthsdk.feature_orders.domain.provideOrderRepository
import com.trolla.healthsdk.feature_orders.domain.usecases.GetOrderDetailsUsecase
import com.trolla.healthsdk.feature_orders.domain.usecases.GetOrdersListUsecase
import com.trolla.healthsdk.feature_orders.presentation.OrderListViewModel
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

    /*getOTP*/
    single { GetOTPOnEmailUsecase(get()) }
    single { LoginEmailViewModel(get()) }

    /*Verify OTP*/
    single { VerifyOTPOnEmailUsecase(get()) }
    single { VerifyOTPOnMobileUsecase(get()) }
    single { LoginOTPVerificationViewModel(get()) }
    single { MobileOTPVerificationViewModel(get()) }

    /*Profile*/
    single { UpdateProfileUsecase(get()) }
    single { RegisterViewModel(get()) }

    /*Dashboard*/
    single { provideDashboardRepository(get()) }
    single { GetDashboardUsecase(get()) }
    single { DashboardViewModel(get()) }

    /*Cart*/
    single { provideCartRepository(get()) }
    single { GetCartDetailsUsecase(get()) }
    single { AddToCartUsercase(get()) }
    single { CreateOrderUsecase(get()) }
    factory { CartViewModel(get(), get(), get()) }

    /*Products List*/
    single { provideProductsListRepository(get()) }
    single { GetProductsListUsecase(get()) }
    single { ProductsListViewModel(get()) }

    /*Product Details*/
    single { provideProductDetailsRepository(get()) }
    single { GetProductDetailsUsecase(get()) }
    single { ProductDetailsViewModel(get()) }

    /*address*/
    single { provideAddressRepository(get()) }
    single { GetAddressListUsecase(get()) }
    single { AddAddressUsecase(get()) }
    single { UpdateAddressUsecase(get()) }
    single { DeleteAddressUsecase(get()) }
    single { AddressListViewModel(get(), get()) }
    single { AddAddressViewModel(get(), get()) }

    /*orders*/
    single { provideOrderRepository(get()) }
    single { GetOrdersListUsecase(get()) }
    single { GetOrderDetailsUsecase(get()) }
    single { OrderListViewModel(get()) }

    /*profile*/
    single { ProfileViewModel() }

    /*settings*/
    single { SettingsViewModel() }

}