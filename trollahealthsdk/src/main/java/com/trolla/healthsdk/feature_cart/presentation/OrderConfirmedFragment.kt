package com.trolla.healthsdk.feature_cart.presentation

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentOrderConfirmedBinding
import com.trolla.healthsdk.feature_cart.data.models.RefreshCartEvent
import com.trolla.healthsdk.feature_dashboard.data.RefreshDashboardEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_orders.presentation.OrdersListFragment
import com.trolla.healthsdk.feature_payment.presentation.PaymentGatewayIntegrationFragment
import com.trolla.healthsdk.utils.hide
import kotlinx.android.synthetic.main.fragment_order_confirmed.*
import org.greenrobot.eventbus.EventBus

class OrderConfirmedFragment : Fragment() {

    companion object {
        fun newInstance(
            orderId: String,
            paymentMode: String?,
            transaction_id: String?,
            amount: String?,
            rarorpay_orderid: String?,
        ): OrderConfirmedFragment {
            var bundle = Bundle()
            bundle.putString("orderId", orderId)
            bundle.putString("paymentMode", paymentMode)
            bundle.putString("transaction_id", transaction_id)
            bundle.putString("amount", amount)
            bundle.putString("rarorpay_orderid", rarorpay_orderid)
            var orderConfirmedFragment = OrderConfirmedFragment()
            orderConfirmedFragment.arguments = bundle
            return orderConfirmedFragment
        }
    }

    val orderId by lazy {
        arguments?.let {
            it.getString("orderId")
        }
    }

    val paymentMode by lazy {
        arguments?.let {
            it.getString("paymentMode")
        }
    }

    val transaction_id by lazy {
        arguments?.let {
            it.getString("transaction_id")
        }
    }

    val amount by lazy {
        arguments?.let {
            it.getString("amount")
        }
    }

    val rarorpay_orderid by lazy {
        arguments?.let {
            it.getString("rarorpay_orderid")
        }
    }

    var counter = 6

    lateinit var binding: FragmentOrderConfirmedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_confirmed,
            container,
            false
        )

        binding.lifecycleOwner = this

        binding.txtOrderDetails.text =
            "Your Order with Order ID $orderId has been placed Successfully."
        binding.txtGotoHomePage.setOnClickListener {
            parentFragmentManager?.popBackStack()
            (activity as DashboardActivity).addOrReplaceFragment(
                OrdersListFragment.newInstance(),
                true
            )
        }

        binding.txtTrackOrder.setOnClickListener {
            parentFragmentManager?.popBackStack()
            (activity as DashboardActivity).addOrReplaceFragment(
                OrdersListFragment.newInstance(),
                true
            )
        }

        EventBus.getDefault().post(RefreshDashboardEvent())
        EventBus.getDefault().post(RefreshCartEvent())

        if (paymentMode == "prepaid") {
            binding.txtGotoHomePage.hide()
            binding.txtTrackOrder.hide()
        } else {
            binding.txtPaymentRedirectionMessage.hide()
        }

        binding.txtPaymentRedirectionMessage.text =
            "Please wait while we take you to payment screen in $counter seconds"

        if (paymentMode == "prepaid") {
            val timer = object : CountDownTimer(5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    counter -= 1
                    binding.txtPaymentRedirectionMessage.text =
                        "Please wait while we take you to payment screen in $counter second(s)"
                }

                override fun onFinish() {
                    parentFragmentManager?.popBackStack()
                    /*(activity as DashboardActivity).addOrReplaceFragment(
                        PaymentGatewayIntegrationFragment.newInstance(
                            transaction_id!!,
                            amount ?: "0",
                            rarorpay_orderid ?: ""
                        ),
                        true
                    )*/

                    (activity as DashboardActivity).cartViewModel.getCartDetails()

                    (activity as DashboardActivity).startRazorPay(
                        amount!!,
                        transaction_id!!,
                        rarorpay_orderid ?: "0"
                    )
                }
            }
            timer.start()
        }

        return binding.root
    }


}