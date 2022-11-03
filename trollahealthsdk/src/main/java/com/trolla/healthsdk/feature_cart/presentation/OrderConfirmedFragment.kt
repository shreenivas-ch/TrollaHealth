package com.trolla.healthsdk.feature_cart.presentation

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.FragmentOrderConfirmedBinding
import com.trolla.healthsdk.feature_dashboard.data.RefreshDashboardEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_orders.presentation.OrderDetailsViewModel
import com.trolla.healthsdk.feature_orders.presentation.OrdersListFragment
import com.trolla.healthsdk.utils.*
import org.greenrobot.eventbus.EventBus
import org.koin.java.KoinJavaComponent

class OrderConfirmedFragment : Fragment() {

    val orderDetailsViewModel: OrderDetailsViewModel by KoinJavaComponent.inject(
        OrderDetailsViewModel::class.java
    )

    companion object {
        fun newInstance(
            orderId: String,
            paymentMode: String?
        ): OrderConfirmedFragment {
            var bundle = Bundle()
            bundle.putString("orderId", orderId)
            bundle.putString("paymentMode", paymentMode)
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
    var counter = 6

    var rarorpay_orderid = ""
    var transaction_id = ""
    var amount = ""

    var isTimerCompleted = false

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
        (activity as DashboardActivity).refreshCart()

        if (paymentMode == "prepaid") {
            binding.txtGotoHomePage.hide()
            binding.txtTrackOrder.hide()
        } else {
            binding.txtPaymentRedirectionMessage.hide()
        }

        binding.txtPaymentRedirectionMessage.text =
            "Please wait while we take you to payment screen in $counter seconds"

        orderDetailsViewModel.getTransactionIDLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {
                    it?.data?.data?.transaction?.let { transaction ->
                        rarorpay_orderid = transaction.payment_gateway_ref.id
                        transaction_id = transaction._id
                        amount = transaction.amount
                    }
                    callPaymentGateway()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )

                    parentFragmentManager?.popBackStack()
                }
            }
        }

        if (paymentMode == "prepaid") {
            val timer = object : CountDownTimer(5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    counter -= 1
                    binding.txtPaymentRedirectionMessage.text =
                        "Please wait while we take you to payment screen in $counter second(s)"
                }

                override fun onFinish() {
                    /*(activity as DashboardActivity).addOrReplaceFragment(
                        PaymentGatewayIntegrationFragment.newInstance(
                            transaction_id!!,
                            amount ?: "0",
                            rarorpay_orderid ?: ""
                        ),
                        true
                    )*/

                    (activity as DashboardActivity).cartViewModel.getCartDetails()

                    isTimerCompleted = true

                    callPaymentGateway()
                }
            }
            timer.start()

            orderDetailsViewModel.getTransactionID(orderId.toString(), "prepaid")
        }

        return binding.root
    }

    fun callPaymentGateway() {
        if (rarorpay_orderid != "" && isTimerCompleted) {
            (activity as DashboardActivity).startRazorPay(
                amount!!,
                transaction_id!!,
                rarorpay_orderid
            )

            (activity as DashboardActivity).transaction_id = transaction_id!!
            (activity as DashboardActivity).paymentRedirectionScreen = "createorder"
            parentFragmentManager?.popBackStack()
        }
    }


}