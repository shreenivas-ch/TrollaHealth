package com.trolla.health.fcm

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.trolla.health.AppConstants.COSHOP_APPID
import com.trolla.healthsdk.TrollaHealthManager

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private val TAG = MyFirebaseMessagingService::class.java.simpleName
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.e("Parent App", "FCM Token: $token")

        TrollaHealthManager.Builder().appid(COSHOP_APPID).context(this).application(application)
            .build().updateFCMToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.e("coshop", "onMessageReceived")

        if (!remoteMessage.data.isNullOrEmpty()) {
            TrollaHealthManager.Builder().appid(COSHOP_APPID).context(this).application(application)
                .build().showNotification(remoteMessage)
        }
    }
}