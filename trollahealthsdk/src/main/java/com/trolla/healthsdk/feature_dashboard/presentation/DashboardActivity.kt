package com.trolla.healthsdk.feature_dashboard.presentation

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.razorpay.PaymentResultListener
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.AWSUtil
import com.trolla.healthsdk.core.InterfaceAWS
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_cart.presentation.CartViewModel
import com.trolla.healthsdk.feature_dashboard.RefreshLocalCartDataEvent
import com.trolla.healthsdk.utils.*
import org.greenrobot.eventbus.EventBus
import org.koin.java.KoinJavaComponent
import java.io.File
import id.zelory.compressor.Compressor
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity(), PaymentResultListener {

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
                    val products = it.data?.data?.products
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

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        LogUtil.printObject("RazorPay: $razorpayPaymentID")
    }

    override fun onPaymentError(code: Int, response: String?) {
        LogUtil.printObject("RazorPay: $code:$response")
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
                        }

                        override fun onSuccess(index: Int, url: String?) {
                            LogUtil.printObject("AWS: Upload Success: $url")
                        }

                        override fun onProgress(index: Int, progress: Float) {
                            LogUtil.printObject("AWS: Upload Success: $progress")
                        }
                    })
            }
        } catch (ex: Exception) {
            LogUtil.printObject("AWS: Upload Error: $ex")
        }
    }
}