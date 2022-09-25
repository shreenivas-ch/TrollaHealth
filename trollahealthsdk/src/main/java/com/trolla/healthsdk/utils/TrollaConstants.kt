package com.trolla.healthsdk.utils

import android.util.Log

object TrollaConstants {
    const val PAGINATION_DEFAULT_INITIAL_PAGE = 1
    const val PAGINATION_DEFAULT_LIMIT = 20

    val DATEFORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'"
    val DATE_FORMAT_2 = "dd MMM yyyy"

    val ADDTOCART_TYPE_PRODUCT = "product"
    val ADDTOCART_TYPE_PRESCRIPTION = "prescription"

    val RAZORPAY_KEYID_TEST = "rzp_test_96LO0xMVkNAHYW"
    val RAZORPAY_KEYID_LIVE = "rzp_live_5q6YIIlWdSQGkZ"

    val ORDERSTATUS_PENDING = "pending"
    val ORDERSTATUS_DELIVERED = "delivered"
    val ORDERSTATUS_CANCELLED = "cancelled"
    val ORDERSTATUS_CANCEL = "cancel"
    val ORDERSTATUS_PROCESSING = "processing"
    val ORDERSTATUS_FAILURE = "failure"
}