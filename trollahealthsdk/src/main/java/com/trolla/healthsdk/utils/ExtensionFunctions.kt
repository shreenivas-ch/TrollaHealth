package com.trolla.healthsdk.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun UiText.asString(context: Context): String {
    return when (this) {
        is UiText.DynamicString -> this.value
        is UiText.StringResource -> context.getString(this.id)
    }
}

fun Context.showkeyboard(view: View) {
    val manager: InputMethodManager? = getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager?

    manager?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

fun Context.hidekeyboard(view: View) {
    val manager: InputMethodManager? = getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager?

    manager?.hideSoftInputFromWindow(
        view.windowToken, 0
    )
}