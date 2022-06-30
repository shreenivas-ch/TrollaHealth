package com.trolla.healthsdk.core

/**
 * Created by Codename07 on 10/08/17.
 */
interface InterfaceAWS {
    fun onError(index: Int, error: String?)
    fun onSuccess(index: Int, url: String?)
    fun onProgress(index: Int, progress: Float)
}