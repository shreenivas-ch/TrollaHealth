package com.trolla.healthsdk.feature_auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentLoginEmailBinding
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

        binding.loginEmailViewModel = loginEmailViewModel

        binding.edtEmail.addTextChangedListener {
            loginEmailViewModel.email.value = it.toString()
        }

        binding.txtLogin.setOnClickListener {
            loginEmailViewModel.login()
        }

        return binding.root
    }
}