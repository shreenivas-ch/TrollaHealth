package com.trolla.healthsdk.di

import com.trolla.healthsdk.feature_auth.domain.provideAuthRepository
import com.trolla.healthsdk.data.remote.RetrofitFactory
import com.trolla.healthsdk.feature_auth.domain.usecases.GetOTPOnEmailUsecase
import com.trolla.healthsdk.feature_auth.domain.usecases.VerifyGetOTPOnMobileUsecase
import com.trolla.healthsdk.feature_auth.domain.usecases.VerifyOTPOnEmailUsecase
import com.trolla.healthsdk.feature_auth.presentation.LoginEmailViewModel
import com.trolla.healthsdk.feature_auth.presentation.LoginOTPVerificationViewModel
import com.trolla.healthsdk.feature_productslist.presentation.ProductsListViewModel
import org.koin.core.scope.get
import org.koin.dsl.module

val repositoryModule = module {
    single { RetrofitFactory.makeRetrofitService() }
    single { provideAuthRepository(get()) }

    single { GetOTPOnEmailUsecase(get()) }
    single { LoginEmailViewModel(get()) }

    single { VerifyOTPOnEmailUsecase(get()) }
    single { VerifyGetOTPOnMobileUsecase(get()) }
    single { LoginOTPVerificationViewModel(get(), get()) }

    single { ProductsListViewModel() }
}