package com.trolla.healthsdk.feature_auth.presentation

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.FragmentLoginEmailBinding
import com.trolla.healthsdk.utils.LogUtil
import org.koin.java.KoinJavaComponent.inject

class LoginEmailFragment : Fragment() {

    val loginEmailViewModel: LoginEmailViewModel by inject(LoginEmailViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<FragmentLoginEmailBinding>(
            inflater,
            R.layout.fragment_login_email,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.loginEmailViewModel = loginEmailViewModel

        binding.txtLogin.setOnClickListener {
            loginEmailViewModel.login()
        }

        loginEmailViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as AuthenticationActivity).showHideProgressBar(it)
        }

        loginEmailViewModel.getOTPResponse.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {
                    (activity as AuthenticationActivity).addOrReplaceFragment(
                        LoginOTPVerificationFragment.getInstance(
                            loginEmailViewModel.emailLiveData.value.toString()
                        ), true
                    )
                }
                is Resource.Error -> {

                }
            }
        }

        return binding.root
    }

}