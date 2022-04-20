package com.trolla.healthsdk.di

import com.trolla.healthsdk.feature_auth.domain.provideAuthRepository
import com.trolla.healthsdk.data.remote.RetrofitFactory
import com.trolla.healthsdk.feature_auth.domain.usecases.LoginUseCase
import com.trolla.healthsdk.feature_auth.presentation.LoginEmailViewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { RetrofitFactory.makeRetrofitService() }
    single { provideAuthRepository(get()) }
    single { LoginUseCase(get()) }
    single { LoginEmailViewModel(get()) }
}