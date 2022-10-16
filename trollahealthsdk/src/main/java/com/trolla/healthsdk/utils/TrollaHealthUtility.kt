package com.trolla.healthsdk.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import android.util.TypedValue
import java.lang.Exception
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

object TrollaHealthUtility {

    fun showAlertDialogue(
        context: Context,
        message: String?,
        dialogInterface: DialogInterface.OnClickListener? = null
    ) {
        var alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage(message ?: "Message")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton(android.R.string.ok, dialogInterface)
        var alert = alertDialogBuilder.create()
        alert.show()
    }

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

    fun getMarginInDp(sizeInDp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, sizeInDp, context.resources
                .displayMetrics
        ).toInt()
    }

    fun getDate(addedOn: String?): String? {

        var modifiedDate = addedOn
        try {
            val format = SimpleDateFormat(TrollaConstants.DATE_FORMAT_3, Locale.getDefault())
            val newFormat = SimpleDateFormat(TrollaConstants.DATE_FORMAT_2, Locale.getDefault())
            format.timeZone = TimeZone.getTimeZone("UTC")
            newFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = format.parse(addedOn)
            modifiedDate =
                newFormat.format(date)
        } catch (ex: Exception) {
            LogUtil.printObject(ex.toString())
        }
        return modifiedDate
    }

    fun convertOrderDate(addedOn: String?): String? {

        var modifiedDate = addedOn
        try {
            val format = SimpleDateFormat(TrollaConstants.DATE_FORMAT_1, Locale.getDefault())
            val newFormat = SimpleDateFormat(TrollaConstants.DATE_FORMAT_4, Locale.getDefault())
            format.timeZone = TimeZone.getTimeZone("UTC")
            newFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = format.parse(addedOn)
            modifiedDate = newFormat.format(date)
        } catch (ex: Exception) {
            LogUtil.printObject(ex.toString())
        }
        return modifiedDate
    }
}