package com.trolla.healthsdk.data

import androidx.annotation.StringRes
import com.trolla.healthsdk.R

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(@StringRes val id: Int) : UiText()
    companion object{
        fun unknownerror():UiText{
            return UiText.StringResource(R.string.error_unknown)
        }
    }
}
