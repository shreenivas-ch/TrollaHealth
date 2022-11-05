package com.trolla.healthsdk.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences


object TrollaPreferencesManager {

    const val ACCESS_TOKEN = "access_token"
    const val IS_PROFILE_COMPLETE = "is_profile_complete"
    const val PROFILE_ID = "profile_id"
    const val PROFILE_NAME = "profile_name"
    const val PROFILE_EMAIL = "profile_email"
    const val PROFILE_DAY = "profile_day"
    const val PROFILE_MONTH = "profile_month"
    const val PROFILE_YEAR = "profile_year"
    const val PROFILE_GENDER = "profile_gender"
    const val PROFILE_MOBILE = "profile_mobile"
    const val PM_DEFAULT_PINCODE = "default_pincode"
    const val PM_DEFAULT_ADDRESS = "default_address"
    const val PM_LOCAL_SEARCH_HISTORY = "local_search_history"
    const val PM_CART_SELECTED_PAYMENT_METHOD = "cart_selected_payment_method"

    const val PM_CART_SELECTED_ADDRESS_ID = "cart_selected_address_id"
    const val PM_CART_SELECTED_ADDRESS_TITLE = "cart_selected_address_title"
    const val PM_CART_SELECTED_ADDRESS_ADDRESS = "cart_selected_address_address"

    lateinit var preferences: SharedPreferences

    private const val PREFERENCES_FILE_NAME = "TrollaSharedPreferences"

    fun with(application: Application) {
        preferences = application.getSharedPreferences(
            PREFERENCES_FILE_NAME, Context.MODE_PRIVATE
        )
    }

    fun clearPreferences() {
        preferences.edit().clear().commit()
    }

    fun setString(value: String?, key: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    fun setBoolean(value: Boolean, key: String) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean? {
        return preferences.getBoolean(key, false)
    }
}