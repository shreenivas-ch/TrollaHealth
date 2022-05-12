package com.trolla.healthsdk.utils

class LiveDataValidatorResolver(private val validators: List<LiveDataValidator>) {
    fun isValid(): Boolean {
        for (validator in validators) {
            if (!validator.isValid()) return false
        }
        return true
    }
}