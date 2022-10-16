package com.trolla.healthsdk

import android.app.Application
import android.content.Context
import android.content.Intent
import com.freshchat.consumer.sdk.Freshchat
import com.google.firebase.messaging.RemoteMessage
import com.trolla.healthsdk.data.PushnotificationDataModel
import com.trolla.healthsdk.data.models.UserAddress
import com.trolla.healthsdk.di.repositoryModule
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_onboarding.presentation.OnboardingActivity
import com.trolla.healthsdk.utils.LogUtil
import com.trolla.healthsdk.utils.NotificationHandler
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


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

        var accessToken =
            TrollaPreferencesManager.getString(TrollaPreferencesManager.ACCESS_TOKEN) ?: ""
        var isProfileComplete =
            TrollaPreferencesManager.getBoolean(TrollaPreferencesManager.IS_PROFILE_COMPLETE)
                ?: false

        if (accessToken == "") {
            context?.startActivity(Intent(context, OnboardingActivity::class.java))
        } else {
            var loginIntent = Intent(context, DashboardActivity::class.java)
            loginIntent.putExtra("action", DashboardActivity.DASHBOARD_ACTION_DASHBOARD)
            context?.startActivity(loginIntent)
        }
    }

    fun updateFCMToken(fcmToken: String?) {
        fcmToken?.let {
            LogUtil.printObject("FCM Token:$fcmToken")
        }

        context?.let {
            Freshchat.getInstance(it).setPushRegistrationToken(fcmToken!!)
        }
    }

    fun showNotification(remoteMessage: RemoteMessage) {

        if (Freshchat.isFreshchatNotification(remoteMessage)) {
            Freshchat.handleFcmMessage(context!!, remoteMessage);
        } else {
            val pushNotificationModel = PushnotificationDataModel()
            pushNotificationModel.title =
                remoteMessage.data["title"] ?: context!!.getString(R.string.app_name)
            pushNotificationModel.body = remoteMessage.data["body"] ?: ""
            pushNotificationModel.type = remoteMessage.data["type"] ?: ""
            pushNotificationModel.element_id = remoteMessage.data["element_id"] ?: ""

            if (remoteMessage.data.containsKey("big_image")) {
                pushNotificationModel.big_image = remoteMessage.data["big_image"] ?: ""
            }

            val resultIntent = Intent(application, DashboardActivity::class.java)
            resultIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context?.let {
                NotificationHandler().generateNotification(it, pushNotificationModel, resultIntent)
            }
        }

    }

}