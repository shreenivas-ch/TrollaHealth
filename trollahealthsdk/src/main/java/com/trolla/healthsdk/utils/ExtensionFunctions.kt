package com.trolla.healthsdk.utils

import android.view.View

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