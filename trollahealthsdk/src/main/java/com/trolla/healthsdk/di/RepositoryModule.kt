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
import com.trolla.healthsdk.feature_cart.domain.usecases.*
import com.trolla.healthsdk.feature_cart.presentation.CartViewModel
import com.trolla.healthsdk.feature_categories.domain.provideCategoriesRepository
import com.trolla.healthsdk.feature_categories.domain.usecases.GetCategoriesUsecase
import com.trolla.healthsdk.feature_categories.presentation.CategoriesViewModel
import com.trolla.healthsdk.feature_dashboard.domain.provideDashboardRepository
import com.trolla.healthsdk.feature_dashboard.domain.usecases.GetDashboardUsecase
import com.trolla.healthsdk.feature_dashboard.domain.usecases.GetProfileUsecase
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardViewModel
import com.trolla.healthsdk.feature_dashboard.presentation.ProfileViewModel
import com.trolla.healthsdk.feature_dashboard.presentation.SettingsViewModel
import com.trolla.healthsdk.feature_orders.domain.provideOrderRepository
import com.trolla.healthsdk.feature_orders.domain.usecases.CancelOrderUsecase
import com.trolla.healthsdk.feature_orders.domain.usecases.GetOrderDetailsUsecase
import com.trolla.healthsdk.feature_orders.domain.usecases.GetOrdersListUsecase
import com.trolla.healthsdk.feature_orders.domain.usecases.RepeatOrderUsecase
import com.trolla.healthsdk.feature_orders.presentation.OrderDetailsViewModel
import com.trolla.healthsdk.feature_orders.presentation.OrderListViewModel
import com.trolla.healthsdk.feature_payment.presentation.PaymentGatewayIntegrationViewModel
import com.trolla.healthsdk.feature_productdetails.domain.provideProductDetailsRepository
import com.trolla.healthsdk.feature_productdetails.domain.usecases.GetProductDetailsUsecase
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsViewModel
import com.trolla.healthsdk.feature_productslist.domain.provideProductsListRepository
import com.trolla.healthsdk.feature_productslist.domain.usecases.GetProductsListUsecase
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListViewModel
import com.trolla.healthsdk.feature_search.domain.provideSearchRepository
import com.trolla.healthsdk.feature_search.domain.usecases.SearchUsecase
import com.trolla.healthsdk.feature_search.presentation.SearchViewModel
import com.trolla.healthsdk.ui_utils.WebviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { RetrofitFactory.makeRetrofitService() }
    single { provideAuthRepository(get()) }

    /*getOTP*/
    single { GetOTPOnEmailUsecase(get()) }
    factory { LoginEmailViewModel(get()) }

    /*Verify OTP*/
    single { VerifyOTPOnEmailUsecase(get()) }
    single { VerifyOTPOnMobileUsecase(get()) }
    factory { LoginOTPVerificationViewModel(get()) }
    factory { MobileOTPVerificationViewModel(get()) }

    /*Profile*/
    single { UpdateProfileUsecase(get()) }
    factory { RegisterViewModel(get()) }

    /*Dashboard*/
    single { provideDashboardRepository(get()) }
    single { GetDashboardUsecase(get()) }
    factory { DashboardViewModel(get(), get()) }

    /*Cart*/
    single { provideCartRepository(get()) }
    single { GetCartDetailsUsecase(get()) }
    single { AddToCartUsercase(get()) }
    single { CreateOrderUsecase(get()) }
    single { UpdatePaymentUsecase(get()) }
    single { GetTransactionIDUsecase(get()) }
    viewModel {
        CartViewModel(get(), get(), get(), get())
    }

    /*Categories*/
    single { provideCategoriesRepository(get()) }
    single { GetCategoriesUsecase(get()) }
    single { CategoriesViewModel(get()) }

    /*Products List*/
    single { provideProductsListRepository(get()) }
    single { GetProductsListUsecase(get()) }
    factory { ProductsListViewModel(get()) }

    /*Search*/
    single { provideSearchRepository(get()) }
    single { SearchUsecase(get()) }
    factory { SearchViewModel(get()) }

    /*Product Details*/
    single { provideProductDetailsRepository(get()) }
    single { GetProductDetailsUsecase(get()) }
    factory { ProductDetailsViewModel(get()) }

    /*address*/
    single { provideAddressRepository(get()) }
    single { GetAddressListUsecase(get()) }
    single { AddAddressUsecase(get()) }
    single { UpdateAddressUsecase(get()) }
    single { DeleteAddressUsecase(get()) }
    factory { AddressListViewModel(get(), get()) }
    factory { AddAddressViewModel(get(), get()) }

    /*orders*/
    single { provideOrderRepository(get()) }
    single { GetOrdersListUsecase(get()) }
    single { GetOrderDetailsUsecase(get()) }
    factory { OrderListViewModel(get()) }
    factory { CancelOrderUsecase(get()) }
    factory { RepeatOrderUsecase(get()) }
    factory { OrderDetailsViewModel(get(), get(), get(), get()) }

    factory { PaymentGatewayIntegrationViewModel() }

    /*profile*/
    single { GetProfileUsecase(get()) }
    factory { ProfileViewModel() }
    factory { WebviewViewModel() }

    /*settings*/
    factory { SettingsViewModel() }

}