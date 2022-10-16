package com.trolla.healthsdk.feature_auth.presentation

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.databinding.LoginOTPVerificationFragmentBinding
import com.trolla.healthsdk.feature_address.presentation.AddAddressViewModel
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import com.trolla.healthsdk.utils.asString
import com.trolla.healthsdk.utils.hidekeyboard
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class LoginOTPVerificationFragment : Fragment() {

    var email: String? = ""

    companion object {
        fun getInstance(email: String): LoginOTPVerificationFragment {
            val bundle = Bundle()
            bundle.putString("email", email)
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
            email = bundle.getString("email")

            if (!email.isNullOrEmpty()) {
                binding.txtOTPSentTo.text = email
                loginOTPVerificationViewModel.email.value = email
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

        binding.btnVerifyOTP.setOnClickListener {
            loginOTPVerificationViewModel.verifyEmailOTP()
        }

        loginOTPVerificationViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        loginOTPVerificationViewModel.verifyOTPResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {

                    var accessToken = it?.data?.data?.access_token
                    var isProfileComplete = it?.data?.data?.is_profile_complete ?: false

                    TrollaPreferencesManager.setString(
                        accessToken,
                        TrollaPreferencesManager.ACCESS_TOKEN
                    )

                    TrollaPreferencesManager.setBoolean(
                        isProfileComplete,
                        TrollaPreferencesManager.IS_PROFILE_COMPLETE
                    )

                    activity?.hidekeyboard(binding.edt1)

                    if (isProfileComplete) {
                        (activity as DashboardActivity).removeAllFragmentFromDashboardBackstack()
                        ((activity as DashboardActivity)).init=false
                        (activity as DashboardActivity).getAddressListOnDashboard()
                    } else {
                        (activity as DashboardActivity).addOrReplaceFragment(
                            RegisterFragmentFragment.getInstance("auth"),
                            true
                        )
                    }

                }
                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        return binding.root
    }
}