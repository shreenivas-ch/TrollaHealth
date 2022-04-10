package com.trolla.healthsdk

import com.trolla.healthsdk.data.models.UserAddress

class TrollaHealthManager private constructor(
    val secretkey: String?,
    val appid: String?,
    val userMobile: String?,
    val userEmail: String?,
    val userFullname: String?,
    val userAddress: UserAddress?
) {
    data class Builder(
        var secretkey: String? = null,
        var appid: String? = null,
        var userMobile: String? = null,
        var userEmail: String? = null,
        var userFullname: String? = null,
        var userAddress: UserAddress? = null
    ) {
        fun secretKey(secretkey: String) = apply { this.secretkey = secretkey }
        fun appid(appid: String) = apply { this.appid = appid }
        fun userMobile(userMobile: String) = apply { this.userMobile = userMobile }
        fun userEmail(userEmail: String) = apply { this.userEmail = userEmail }
        fun userFullName(userName: String) = apply { this.userFullname = userFullname }
        fun userAddress(userAddress: UserAddress) = apply { this.userAddress = userAddress }
        fun build() =
            TrollaHealthManager(secretkey, appid, userMobile, userEmail, userFullname, userAddress)
    }
}