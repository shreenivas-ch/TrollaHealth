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
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class LoginOTPVerificationFragment : Fragment() {

    var type = ""
    var email: String? = ""
    var mobile: String? = ""

    companion object {
        fun getInstance(email: String, mobile: String, type: String): LoginOTPVerificationFragment {
            val bundle = Bundle()
            bundle.putString("email", email)
            bundle.putString("mobile", mobile)
            bundle.putString("type", type)
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
            mobile = bundle.getString("mobile")

            if (!email.isNullOrEmpty()) {
                binding.txtOTPSentTo.text = email
                loginOTPVerificationViewModel.email.value = email
            }

            if (!mobile.isNullOrEmpty()) {
                binding.txtOTPSentTo.text = mobile
                loginOTPVerificationViewModel.mobile.value = mobile
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
            loginOTPVerificationViewModel.verifyOTP()
        }

        loginOTPVerificationViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as AuthenticationActivity).showHideProgressBar(it)
        }

        loginOTPVerificationViewModel.verifyOTPResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (!it.data?.data?.is_profile_complete!!) {

                        TrollaPreferencesManager.put(
                            it?.data?.data?.access_token,
                            "access_token"
                        )

                        if (type == "email") {

                            (activity as AuthenticationActivity).addOrReplaceFragment(
                                RegisterFragmentFragment.getInstance(email!!),
                                true
                            )

                        } else {
                            startActivity(Intent(activity, DashboardActivity::class.java))
                        }
                    } else {

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