package com.trolla.health

import android.app.Application
import `in`.co.localnetworklogs.LocalNetworkLogsManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LocalNetworkLogsManager.getInstance().initiate(this)
    }
}