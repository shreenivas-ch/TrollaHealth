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
        fun getInstance(email: String): RegisterFragmentFragment {
            return RegisterFragmentFragment()
        }
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
        binding.registerViewModel = registerViewModel

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                registerViewModel.dobDateLiveData.value = dayOfMonth.toString()
                registerViewModel.dobMonthLiveData.value = monthOfYear.toString()
                registerViewModel.dobYearLiveData.value = year.toString()
            }
        }

        binding.radioMale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                registerViewModel.genderLiveData.value = registerViewModel.genderMaleConstant
            }
        }

        binding.radioFemale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                registerViewModel.genderLiveData.value = registerViewModel.genderFemaleConstant
            }
        }

        return binding.root
    }

}