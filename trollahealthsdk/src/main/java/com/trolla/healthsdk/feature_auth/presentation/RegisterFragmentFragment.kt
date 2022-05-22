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
import java.util.*

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

        var cal = Calendar.getInstance()
        binding.datePicker.maxDate = cal.timeInMillis
        cal.add(Calendar.YEAR, -18)
        binding.datePicker.updateDate(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) - 1,
            cal.get(Calendar.DATE)
        )
        registerViewModel.dobDateLiveData.value = cal.get(Calendar.DATE).toString()
        registerViewModel.dobMonthLiveData.value = cal.get(Calendar.MONTH).toString()
        registerViewModel.dobYearLiveData.value = cal.get(Calendar.YEAR).toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
                var d = dayOfMonth.toString()
                if (d.length == 1) {
                    d = "0$d"
                }

                var m = monthOfYear.toString()
                if (m.length == 1) {
                    m = "0$m"
                }

                registerViewModel.dobDateLiveData.value = d
                registerViewModel.dobMonthLiveData.value = m

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