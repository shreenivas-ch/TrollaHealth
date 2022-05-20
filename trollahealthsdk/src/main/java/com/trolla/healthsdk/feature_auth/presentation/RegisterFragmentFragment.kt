package com.trolla.healthsdk.feature_auth.presentation

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.RegisterFragmentBinding
import org.koin.java.KoinJavaComponent.inject

class RegisterFragmentFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragmentFragment()
    }

    val registerViewModel: RegisterViewModel by inject(
        RegisterViewModel::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<RegisterFragmentBinding>(
            inflater,
            R.layout.register_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = registerViewModel

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->

            }
        }

        return binding.root
    }

}