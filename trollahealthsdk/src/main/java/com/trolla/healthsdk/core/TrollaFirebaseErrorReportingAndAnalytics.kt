package com.trolla.healthsdk.core

import com.google.firebase.crashlytics.FirebaseCrashlytics

object TrollaFirebaseErrorReportingAndAnalytics {

    fun reportNonFatalError(errorCode: String, errorMessage: String, e: Exception) {
        FirebaseCrashlytics.getInstance().setCustomKey("errorCode", errorCode)
        FirebaseCrashlytics.getInstance().setCustomKey("errorMessage", errorMessage)
        FirebaseCrashlytics.getInstance().recordException(e)
    }
}