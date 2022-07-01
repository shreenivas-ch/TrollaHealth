package com.trolla.healthsdk.core

import android.content.Context
import com.trolla.healthsdk.utils.LogUtil.printObject
import com.trolla.healthsdk.utils.TrollaHealthUtility.showAlertDialogue
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.CognitoCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.*
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.regions.Region
import java.io.File
import java.lang.Exception

object AWSUtil {
    private const val BUCKET_NAME = "mobileapp-prescriptions"
    private const val BUCKET_REGION = "ap-south-1"
    private const val POST_IMAGES_LINK = "https://cdn.mecuresmartbuy.com"
    private const val POOL_ID = "ap-south-1:8def6a35-0e8c-461f-8937-6411ced57dee"
    var PATH_PROFILE_PIC = "/msb/profile_image/"
    private const val PATH_PRESCRIPTION = "/msb/prescription_image/"
    private var sS3Client: AmazonS3Client? = null
    private var sTransferUtility: TransferUtility? = null
    private fun getS3Client(context: Context): AmazonS3Client? {
        if (sS3Client == null) {
            //sS3Client = new AmazonS3Client(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
            sS3Client = AmazonS3Client(CognitoCredentialsProvider(POOL_ID, Regions.EU_WEST_2))
            sS3Client!!.setRegion(Region.getRegion(Regions.fromName(BUCKET_REGION)))
        }
        return sS3Client
    }

    private fun getTransferUtility(ctx: Context): TransferUtility? {
        if (sTransferUtility == null) {
            sTransferUtility = TransferUtility(
                getS3Client(ctx.applicationContext),
                ctx.applicationContext
            )
        }
        return sTransferUtility
    }

    fun uploadFile(ctx: Context, file: File, index: Int, interfaceAWS: InterfaceAWS) {
        val userid = ""
        val transferUtility = getTransferUtility(ctx)
        transferUtility!!.getTransfersWithType(TransferType.UPLOAD)
        val path = PATH_PRESCRIPTION + userid
        val transfer = getTransferUtility(ctx)!!
            .upload(
                BUCKET_NAME + "" + path, file.name,
                file, CannedAccessControlList.PublicRead
            )
        transfer.setTransferListener(object : TransferListener {
            override fun onError(id: Int, e: Exception) {
                printObject("Error during upload: $e")
                showAlertDialogue(ctx, "Error while uploading images, please try again", null)
                interfaceAWS.onError(index, e.toString())
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val progress = (bytesCurrent.toDouble() * 100 / bytesTotal).toInt()
                printObject("onProgressChanged: $id, total: $bytesTotal, current: $bytesCurrent: $progress %")
                interfaceAWS.onProgress(index, progress.toFloat())
            }

            override fun onStateChanged(id: Int, newState: TransferState) {
                printObject("onStateChanged: $id, $newState")
                if (newState == TransferState.COMPLETED) {
                    val url = POST_IMAGES_LINK + path + "/" + file.name
                    interfaceAWS.onSuccess(index, url)
                }
            }
        })
    }
}