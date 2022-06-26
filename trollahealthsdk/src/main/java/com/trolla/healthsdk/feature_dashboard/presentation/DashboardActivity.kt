package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.feature_cart.presentation.CartViewModel
import com.trolla.healthsdk.feature_dashboard.data.RefreshDashboardEvent
import com.trolla.healthsdk.utils.LogUtil
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import com.trolla.healthsdk.utils.setVisibilityOnBoolean
import org.greenrobot.eventbus.EventBus
import org.koin.java.KoinJavaComponent

class DashboardActivity : AppCompatActivity() {

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

                    EventBus.getDefault().post(RefreshDashboardEvent())
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
                    val response = cartViewModel.cartDetailsResponseLiveData.value
                    cartItemsIdsArray.clear()
                    for (i in response?.data?.data?.products?.indices ?: arrayListOf()) {
                        cartItemsIdsArray.add(response?.data?.data?.products?.get(i)?.product?.product_id.toString())
                    }

                    if (!init) {
                        addOrReplaceFragment(HomeFragment())
                        init = true
                    }

                    EventBus.getDefault().post(RefreshDashboardEvent())

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

    fun addOrReplaceFragment(fragment: Fragment, isAdd: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
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
}