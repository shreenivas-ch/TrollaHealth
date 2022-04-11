package com.trolla.healthsdk.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object TrollaHealthUtility {

    fun getCertificateHash(context: Context): String? {
        var hashKey = ""
        try {
            val info: PackageInfo = context.packageManager
                .getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)

            var signInInfoArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.signingInfo.apkContentsSigners
            } else {
                info.signatures
            }

            for (signature in signInInfoArray) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT)
            }

        } catch (e: PackageManager.NameNotFoundException) {
            LogUtil.printObject(e)
        } catch (e: NoSuchAlgorithmException) {
            LogUtil.printObject(e)
        }
        return hashKey.trim { it <= ' ' }
    }
}