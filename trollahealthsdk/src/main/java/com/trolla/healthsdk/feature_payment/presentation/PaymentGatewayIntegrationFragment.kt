package com.trolla.healthsdk.feature_payment.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trolla.healthsdk.R

class PaymentGatewayIntegrationFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentGatewayIntegrationFragment()
    }

    private lateinit var viewModel: PaymentGatewayIntegrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payment_gateway_integration_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PaymentGatewayIntegrationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}