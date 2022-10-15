package com.trolla.healthsdk.utils

object TrollaConstants {
    const val PAGINATION_DEFAULT_INITIAL_PAGE = 1
    const val PAGINATION_DEFAULT_LIMIT = 20

    val DATE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.sss'Z'"
    val DATE_FORMAT_2 = "dd MMM yyyy hh:mm a"
    val DATE_FORMAT_3 = "yyyy-MM-dd HH:mm:ss"
    val DATE_FORMAT_4 = "dd-MMM-yyyy"

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
    val ORDERSTATUS_IN_TRANSIT = "in transit"
}