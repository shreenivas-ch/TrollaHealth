package com.trolla.healthsdk.feature_auth.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.databinding.LoginOTPVerificationFragmentBinding
import com.trolla.healthsdk.feature_address.presentation.AddAddressViewModel
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class LoginOTPVerificationFragment : Fragment() {

    companion object {
        fun getInstance(email: String, mobile: String): LoginOTPVerificationFragment {
            val bundle = Bundle()
            bundle.putString("email", email)
            bundle.putString("mobile", mobile)
            val fragment = LoginOTPVerificationFragment()
            fragment.arguments = bundle
            return fragment
        }
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

        binding.lifecycleOwner = this
        binding.loginOTPVerificationViewModel = loginOTPVerificationViewModel

        var bundle = arguments
        bundle?.let {
            var email = bundle.getString("email")
            var mobile = bundle.getString("mobile")

            if (!email.isNullOrEmpty()) {
                binding.txtOTPSentTo.text = email
            }

            if (!mobile.isNullOrEmpty()) {
                binding.txtOTPSentTo.text = mobile
            }

        }

        binding.edt1.requestFocus()

        binding.edt1.addTextChangedListener {
            if (it.toString().length == 1) {
                binding.edt2.requestFocus()
            }
        }

        binding.edt2.addTextChangedListener {
            if (it.toString().length == 1) {
                binding.edt3.requestFocus()
            } else if (it.toString().isNullOrEmpty()) {
                binding.edt1.requestFocus()
            }
        }

        binding.edt3.addTextChangedListener {
            if (it.toString().length == 1) {
                binding.edt4.requestFocus()
            } else if (it.toString().isNullOrEmpty()) {
                binding.edt2.requestFocus()
            }
        }

        binding.edt4.addTextChangedListener {
            if (it.toString().isNullOrEmpty()) {
                binding.edt3.requestFocus()
            }
        }

        loginOTPVerificationViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as AuthenticationActivity).showHideProgressBar(it)
        }


        return binding.root
    }

}