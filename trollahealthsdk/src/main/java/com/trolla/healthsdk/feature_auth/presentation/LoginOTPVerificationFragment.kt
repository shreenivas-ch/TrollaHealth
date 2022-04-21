package com.trolla.healthsdk.feature_auth.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trolla.healthsdk.R

class LoginOTPVerificationFragment : Fragment() {

    companion object {
        fun newInstance() = LoginOTPVerificationFragment()
    }

    private lateinit var viewModel: LoginOTPVerificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_o_t_p_verification_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginOTPVerificationViewModel::class.java]
    }

}