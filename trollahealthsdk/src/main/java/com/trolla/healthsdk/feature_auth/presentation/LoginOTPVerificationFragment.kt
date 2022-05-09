package com.trolla.healthsdk.feature_auth.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.databinding.LoginOTPVerificationFragmentBinding
import com.trolla.healthsdk.feature_address.presentation.AddAddressViewModel
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class LoginOTPVerificationFragment : Fragment() {

    companion object {
        fun newInstance() = LoginOTPVerificationFragment()
    }

    val loginOTPVerificationViewModel: LoginOTPVerificationViewModel by inject(
        LoginOTPVerificationViewModel::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<LoginOTPVerificationFragmentBinding>(
            inflater,
            R.layout.login_o_t_p_verification_fragment,
            container,
            false
        )

        binding.loginEmailViewModel = loginOTPVerificationViewModel

        return binding.root
    }

}