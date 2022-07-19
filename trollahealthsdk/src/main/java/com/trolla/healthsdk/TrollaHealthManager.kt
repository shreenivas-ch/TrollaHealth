package com.trolla.healthsdk

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import com.trolla.healthsdk.data.models.UserAddress
import com.trolla.healthsdk.di.repositoryModule
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_auth.presentation.AuthenticationActivity
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_onboarding.presentation.OnboardingActivity
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class TrollaHealthManager private constructor(
    val context: Context?,
    val application: Application?,
    val secretkey: String?,
    val appid: String?,
    val userMobile: String?,
    val userEmail: String?,
    val userFullname: String?,
    val userAddress: UserAddress?
) {
    data class Builder(
        var context: Context? = null,
        var application: Application? = null,
        var secretkey: String? = null,
        var appid: String? = null,
        var userMobile: String? = null,
        var userEmail: String? = null,
        var userFullname: String? = null,
        var userAddress: UserAddress? = null
    ) {
        fun context(context: Context) = apply { this.context = context }
        fun application(application: Application) = apply { this.application = application }
        fun secretKey(secretkey: String) = apply { this.secretkey = secretkey }
        fun appid(appid: String) = apply { this.appid = appid }
        fun userMobile(userMobile: String) = apply { this.userMobile = userMobile }
        fun userEmail(userEmail: String) = apply { this.userEmail = userEmail }
        fun userFullName(userName: String) = apply { this.userFullname = userFullname }
        fun userAddress(userAddress: UserAddress) = apply { this.userAddress = userAddress }
        fun build() =
            TrollaHealthManager(
                context,
                application,
                secretkey,
                appid,
                userMobile,
                userEmail,
                userFullname,
                userAddress
            )
    }

    fun launch() {
        startKoin {
            context?.let { androidContext(context) }
            modules(listOf(repositoryModule))
        }
        application?.let {
            TrollaPreferencesManager.with(application)
        }

        var accessToken = TrollaPreferencesManager.get(TrollaPreferencesManager.ACCESS_TOKEN) ?: ""
        var isProfileComplete =
            TrollaPreferencesManager.get(TrollaPreferencesManager.IS_PROFILE_COMPLETE) ?: false

        if (accessToken == "" || !isProfileComplete) {
            context?.startActivity(Intent(context, OnboardingActivity::class.java))
        } else {
            context?.startActivity(Intent(context, DashboardActivity::class.java))
        }
    }

    fun closeHealthSDK() {
        stopKoin()
    }
}