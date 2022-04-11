package com.trolla.healthsdk

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import com.trolla.healthsdk.data.models.UserAddress
import com.trolla.healthsdk.feature_auth.presentation.AuthenticationActivity

class TrollaHealthManager private constructor(
    val context: Context?,
    val secretkey: String?,
    val appid: String?,
    val userMobile: String?,
    val userEmail: String?,
    val userFullname: String?,
    val userAddress: UserAddress?
) {
    data class Builder(
        var context: Context? = null,
        var secretkey: String? = null,
        var appid: String? = null,
        var userMobile: String? = null,
        var userEmail: String? = null,
        var userFullname: String? = null,
        var userAddress: UserAddress? = null
    ) {
        fun context(context: Context) = apply { this.context = context }
        fun secretKey(secretkey: String) = apply { this.secretkey = secretkey }
        fun appid(appid: String) = apply { this.appid = appid }
        fun userMobile(userMobile: String) = apply { this.userMobile = userMobile }
        fun userEmail(userEmail: String) = apply { this.userEmail = userEmail }
        fun userFullName(userName: String) = apply { this.userFullname = userFullname }
        fun userAddress(userAddress: UserAddress) = apply { this.userAddress = userAddress }
        fun build() =
            TrollaHealthManager(
                context,
                secretkey,
                appid,
                userMobile,
                userEmail,
                userFullname,
                userAddress
            )
    }

    fun launch() {
        context?.startActivity(Intent(context, AuthenticationActivity::class.java))
    }
}