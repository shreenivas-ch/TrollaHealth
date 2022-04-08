package com.trolla.healthsdk.utils

import android.util.Log

object LogUtil {
    fun printObject(o: Any) {
        Log.d("TrollaHealth", o.toString())
    }
}