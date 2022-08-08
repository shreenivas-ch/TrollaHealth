package com.trolla.healthsdk.feature_dashboard.presentation

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.AWSUtil
import com.trolla.healthsdk.core.InterfaceAWS
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_cart.data.models.PrescriptionUploadedEvent
import com.trolla.healthsdk.feature_cart.presentation.CartViewModel
import com.trolla.healthsdk.feature_dashboard.RefreshLocalCartDataEvent
import com.trolla.healthsdk.utils.*
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import org.koin.core.context.stopKoin
import org.koin.java.KoinJavaComponent
import java.io.File

class DashboardActivity : AppCompatActivity(),
    PaymentResultWithDataListener {

    var init = false
    var cartItemsIdsArray = ArrayList<String>()
    val cartViewModel: CartViewModel by KoinJavaComponent.inject(CartViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        cartViewModel.addToCartResponseLiveData.observe(this) {
            when (it) {
                is Resource.Success -> {
                    val response = cartViewModel.addToCartResponseLiveData.value
                    cartItemsIdsArray.clear()
                    for (i in response?.data?.data?.cart?.products?.indices ?: arrayListOf()) {
                        cartItemsIdsArray.add(response?.data?.data?.cart?.products?.get(i)?.product?.product_id.toString())
                    }

                    EventBus.getDefault().post(RefreshLocalCartDataEvent())
                    //EventBus.getDefault().post(RefreshDashboardEvent())
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        this@DashboardActivity,
                        it.uiText?.asString(this@DashboardActivity)
                    )
                }
            }
        }

        cartViewModel.cartDetailsResponseLiveData.observe(this) {
            when (it) {
                is Resource.Success -> {
                    val products = it.data?.data?.cart?.products
                    cartItemsIdsArray.clear()
                    for (i in products?.indices ?: arrayListOf()) {
                        cartItemsIdsArray.add(products?.get(i)?.product?.product_id.toString())
                    }

                    LogUtil.printObject("-----> $cartItemsIdsArray")

                    if (!init) {
                        addOrReplaceFragment(HomeFragment(), animationRequired = false)
                        init = true
                    }

                    EventBus.getDefault().post(RefreshLocalCartDataEvent())

                }

                is Resource.Error -> {

                    if (!init) {
                        addOrReplaceFragment(HomeFragment(), animationRequired = false)
                        init = true
                    }

                    TrollaHealthUtility.showAlertDialogue(
                        this@DashboardActivity,
                        it.uiText?.asString(this@DashboardActivity)
                    )
                }
            }
        }

        cartViewModel.updatePaymentResponseLiveData.observe(this) {
            when (it) {
                is Resource.Success -> {

                    TrollaHealthUtility.showAlertDialogue(
                        this@DashboardActivity,
                        it.uiText?.asString(this@DashboardActivity)
                    )

                }

                is Resource.Error -> {

                    TrollaHealthUtility.showAlertDialogue(
                        this@DashboardActivity,
                        it.uiText?.asString(this@DashboardActivity)
                    )
                }
            }
        }

        cartViewModel.getCartDetails()
    }

    fun addOrReplaceFragment(
        fragment: Fragment,
        isAdd: Boolean = false,
        animationRequired: Boolean = true
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        if (animationRequired) {
            transaction.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
        }
        if (isAdd) {
            transaction.add(R.id.contentContainer, fragment)
            transaction.addToBackStack(fragment::class.java.simpleName)
        } else {
            transaction.replace(R.id.contentContainer, fragment)
        }
        transaction.commit()
    }

    fun showHideProgressBar(isShow: Boolean = false) {
        findViewById<ProgressBar>(R.id.progressBar).setVisibilityOnBoolean(isShow, true)
    }

    fun startRazorPay(amount: String, transaction_id: String, rarorpay_orderid: String?) {

        var userData =
            TrollaPreferencesManager.get<UpdateProfileResponse>(TrollaPreferencesManager.USER_DATA)
        val roundedOffAmount = Math.round((amount!!).toFloat() * 100)
        val checkout = Checkout()
        checkout.setKeyID(TrollaConstants.RAZORPAY_KEYID_TEST)
        checkout.setImage(R.drawable.appicon)

        try {
            val options = JSONObject()
            options.put("name", "InstaStack")
            options.put("description", "Transaction ID: $transaction_id")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            //options.put("order_id", rarorpay_orderid) //from response of step 3.
            options.put("receipt", transaction_id) //from response of step 3.
            options.put("theme.color", "#6757d7")
            options.put("currency", "INR")
            options.put("amount", roundedOffAmount) //pass amount in currency subunits
            options.put("prefill.name", userData?.name)
            options.put("prefill.email", userData?.email)
            options.put("prefill.contact", userData?.mobile)

            var notesObject = JSONObject()
            notesObject.put("transactionid", transaction_id)

            var hideObject = JSONObject()
            hideObject.put("contact", true)
            hideObject.put("email", true)

            options.put("notes", notesObject)
            options.put("hidden", hideObject)

            checkout.open(this, options)

        } catch (e: Exception) {
            TrollaHealthUtility.showAlertDialogue(
                this,
                "Error in starting Razorpay Checkout"
            )
        }
    }

    var imagePickerLauncherFrom = ""
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                LogUtil.printObject("ImagePicker: Image:  $uri")
                uri?.let {
                    uploadImageToS3Bucket(uri)
                }
            }
        }

    fun launchImagePicker(launcherFrom: String, pickerType: ImageProvider) {
        imagePickerLauncherFrom = launcherFrom
        ImagePicker.with(this)
            .provider(ImageProvider.BOTH) //Or bothCameraGallery()
            .createIntentFromDialog { launcher.launch(it) }
    }

    fun uploadImageToS3Bucket(uri: Uri) {

        var userData =
            TrollaPreferencesManager.get<UpdateProfileResponse>(TrollaPreferencesManager.USER_DATA)

        var userid = userData?._id

        var filePath = FetchPath.getPath(this@DashboardActivity, uri)
        val pathtoupload = AWSUtil.PATH_PRESCRIPTION + userid

        try {
            lifecycleScope.launch {
                val compressedImageFile =
                    Compressor.compress(this@DashboardActivity, File(filePath))
                AWSUtil.uploadFile(
                    this@DashboardActivity,
                    compressedImageFile,
                    0,
                    pathtoupload,
                    object : InterfaceAWS {
                        override fun onError(index: Int, error: String?) {
                            LogUtil.printObject("AWS: UploadError:  $error")
                            showPrescriptionUploadedFailedDialogue(error ?: "Something went wrong")
                        }

                        override fun onSuccess(index: Int, url: String?) {
                            LogUtil.printObject("AWS: Upload Success: $url")
                            EventBus.getDefault().post(PrescriptionUploadedEvent(url!!))
                        }

                        override fun onProgress(index: Int, progress: Float) {
                            LogUtil.printObject("AWS: Upload Success: $progress")
                            progressDialogueSubTitle.text =
                                "Please wait while we are uploading your prescription\n$progress file uploaded"
                        }
                    })

                prepareProgressDialogue(
                    "File Uploading",
                    "Please wait while we are uploading your prescription"
                )
            }
        } catch (ex: Exception) {
            LogUtil.printObject("AWS: Upload Error: $ex")
        }
    }

    lateinit var progressDialogueTitle: TextView
    lateinit var progressDialogueSubTitle: TextView
    lateinit var llOptions: LinearLayout
    lateinit var txtViewTerms: TextView
    lateinit var txtOKButton: TextView

    var dialog: Dialog? = null

    fun prepareProgressDialogue(title: String, subtitle: String) {
        dialog = Dialog(this@DashboardActivity)
        dialog?.setContentView(R.layout.layout_fileupload_progress_dialog)
        dialog?.setTitle(null)
        dialog?.setCanceledOnTouchOutside(false)

        progressDialogueTitle = dialog?.findViewById<View>(R.id.txtTitle) as TextView
        progressDialogueSubTitle = dialog?.findViewById<View>(R.id.txtSubtitle) as TextView
        llOptions = dialog?.findViewById<View>(R.id.llOptions) as LinearLayout
        txtViewTerms = dialog?.findViewById<View>(R.id.txtViewTerms) as TextView
        txtOKButton = dialog?.findViewById<View>(R.id.txtOKButton) as TextView

        progressDialogueTitle.text = title
        progressDialogueSubTitle.text = subtitle

        txtViewTerms.setOnClickListener { v: View? -> dialog?.dismiss() }
        txtOKButton.setOnClickListener { v: View? -> dialog?.dismiss() }

        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.let {
            if (!it.isShowing) {
                it.show()
            }
        }
    }

    fun showPrescriptionUploadedSuccessDialogue() {

        dialog?.let {
            progressDialogueTitle.text = "Prescription Uploaded Successfully"
            progressDialogueSubTitle.text =
                "Your prescription needes to be validated to process your order successfully"
            txtViewTerms.show()
        }
    }

    fun showPrescriptionUploadedFailedDialogue(message: String) {

        dialog?.let {
            progressDialogueTitle.text = "Failed to Upload Prescription"
            progressDialogueSubTitle.text = message
            txtViewTerms.hide()
            txtOKButton.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }

    var transaction_id: String = ""

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        LogUtil.printObject("RazorPay: $razorpayPaymentId:${paymentData?.data}")
        cartViewModel.updatePayment(transaction_id, paymentData?.data ?: JSONObject())
    }

    override fun onPaymentError(errorCode: Int, p1: String?, paymentData: PaymentData?) {
        LogUtil.printObject("RazorPay: $errorCode:$paymentData")
        cartViewModel.updatePayment(transaction_id, paymentData?.data ?: JSONObject())
    }

    /*override fun onPaymentSuccess(razorpayPaymentID: String?) {
        LogUtil.printObject("RazorPay: $razorpayPaymentID")
    }

    override fun onPaymentError(code: Int, response: String?) {
        LogUtil.printObject("RazorPay: $code:$response")
    }*/
}