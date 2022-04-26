package com.trolla.healthsdk.feature_payment.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.databinding.PaymentGatewayIntegrationFragmentBinding
import com.trolla.healthsdk.feature_address.presentation.AddAddressViewModel
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class PaymentGatewayIntegrationFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentGatewayIntegrationFragment()
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

        binding.viewModel = paymentGatewayIntegrationViewModel

        return binding.root
    }

}