package com.trolla.healthsdk.feature_payment.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.razorpay.Checkout
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.PaymentGatewayIntegrationFragmentBinding
import com.trolla.healthsdk.utils.TrollaConstants.RAZORPAY_KEYID_TEST
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import org.json.JSONObject
import org.koin.java.KoinJavaComponent.inject


class PaymentGatewayIntegrationFragment : Fragment() {

    val transaction_id by lazy {
        arguments?.let {
            it.getString("transanction_id")
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

    companion object {
        fun newInstance(
            transanction_id: String,
            amount: String,
            rarorpay_orderid: String
        ): PaymentGatewayIntegrationFragment {
            var bundle = Bundle()
            bundle.putString("transanction_id", transanction_id)
            bundle.putString("amount", amount)
            bundle.putString("rarorpay_orderid", rarorpay_orderid)
            var paymentGatewayIntegrationFragment = PaymentGatewayIntegrationFragment()
            paymentGatewayIntegrationFragment.arguments = bundle
            return paymentGatewayIntegrationFragment
        }
    }

    val paymentGatewayIntegrationViewModel: PaymentGatewayIntegrationViewModel by inject(
        PaymentGatewayIntegrationViewModel::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<PaymentGatewayIntegrationFragmentBinding>(
            inflater,
            R.layout.payment_gateway_integration_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = paymentGatewayIntegrationViewModel

        paymentGatewayIntegrationViewModel.headerTitle.value =
            "Pay " + getString(R.string.amount, amount)
        paymentGatewayIntegrationViewModel.headerBottomLine.value = 1
        paymentGatewayIntegrationViewModel.headerBackButton.value = 1

        startRazorPay()

        return binding.root
    }

    fun startRazorPay() {

        val roundedOffAmount = Math.round((amount!!).toFloat() * 100)
        val checkout = Checkout()
        checkout.setKeyID(RAZORPAY_KEYID_TEST)
        checkout.setImage(R.drawable.appicon_for_payment_gateway)

        try {
            val options = JSONObject()
            options.put("name", "InstaStack")
            options.put("description", "Transaction ID: $transaction_id")
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("order_id", rarorpay_orderid) //from response of step 3.
            options.put("receipt", transaction_id) //from response of step 3.
            options.put("theme.color", "#6757d7")
            options.put("currency", "INR")
            options.put("amount", roundedOffAmount) //pass amount in currency subunits
            options.put("prefill.name", TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_NAME))
            options.put("prefill.email",  TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_EMAIL))
            options.put("prefill.contact",  TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_MOBILE))

            var notesObject = JSONObject()
            notesObject.put("transactionid", transaction_id)

            var hideObject = JSONObject()
            hideObject.put("contact", true)
            hideObject.put("email", true)

            options.put("notes", notesObject)
            options.put("hidden", hideObject)

            checkout.open(activity, options)

        } catch (e: Exception) {
            TrollaHealthUtility.showAlertDialogue(
                requireContext(),
                "Error in starting Razorpay Checkout"
            )
        }
    }

}