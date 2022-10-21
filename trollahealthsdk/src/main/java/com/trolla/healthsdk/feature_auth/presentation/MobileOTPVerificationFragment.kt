package com.trolla.healthsdk.feature_auth.presentation

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.MobileOTPVerificationFragmentBinding
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import com.trolla.healthsdk.utils.asString
import org.koin.java.KoinJavaComponent.inject

class MobileOTPVerificationFragment : Fragment() {

    var mobile: String? = ""

    companion object {
        fun getInstance(
            mobile: String
        ): MobileOTPVerificationFragment {
            val bundle = Bundle()
            bundle.putString("mobile", mobile)
            val fragment = MobileOTPVerificationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    val mobileOTPVerificationViewModel: MobileOTPVerificationViewModel by inject(
        MobileOTPVerificationViewModel::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<MobileOTPVerificationFragmentBinding>(
            inflater,
            R.layout.mobile_o_t_p_verification_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.mobileOTPVerificationViewModel = mobileOTPVerificationViewModel

        var bundle = arguments
        bundle?.let {
            mobile = bundle.getString("mobile")

            if (!mobile.isNullOrEmpty()) {
                binding.txtOTPSentTo.text = mobile
                mobileOTPVerificationViewModel.mobile.value = mobile
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
            mobileOTPVerificationViewModel.verifyMobileOTP()
        }

        mobileOTPVerificationViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        mobileOTPVerificationViewModel.verifyOTPResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {

                    var accessToken = it?.data?.data?.access_token
                    var isProfileComplete = it?.data?.data?.is_profile_complete

                    var local_isProfileComplete = if (isProfileComplete.isNullOrEmpty()) {
                        false
                    } else isProfileComplete != "false"

                    TrollaPreferencesManager.setString(
                        accessToken,
                        TrollaPreferencesManager.ACCESS_TOKEN
                    )

                    TrollaPreferencesManager.setBoolean(
                        local_isProfileComplete,
                        TrollaPreferencesManager.IS_PROFILE_COMPLETE
                    )

                    if (local_isProfileComplete) {
                        (activity as DashboardActivity).removeAllFragmentFromDashboardBackstack()
                        ((activity as DashboardActivity)).init = false
                        (activity as DashboardActivity).getAddressListOnDashboard()
                    } else {
                        TrollaHealthUtility.showAlertDialogue(
                            requireContext(),
                            getString(R.string.profile_not_updated)
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