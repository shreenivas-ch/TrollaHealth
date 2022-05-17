package com.trolla.healthsdk.utils

import android.content.Context
import android.view.View
import com.trolla.healthsdk.data.UiText

fun View.setVisibilityOnBoolean(condition: Boolean, visibleIf: Boolean) {
    visibility = if (condition == visibleIf) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun UiText.asString(context: Context): String {
    return when(this) {
        is UiText.DynamicString -> this.value
        is UiText.StringResource -> context.getString(this.id)
    }
}