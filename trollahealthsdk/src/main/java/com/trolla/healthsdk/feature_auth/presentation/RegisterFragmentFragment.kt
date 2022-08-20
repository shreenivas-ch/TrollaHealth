package com.trolla.healthsdk.feature_auth.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.RegisterFragmentBinding
import com.trolla.healthsdk.utils.LogUtil
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import com.trolla.healthsdk.utils.asString
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

        registerViewModel.headerTitle.value = "Register"

        var cal = Calendar.getInstance()
        binding.datePicker.maxDate = cal.timeInMillis
        cal.add(Calendar.YEAR, -18)
        binding.datePicker.updateDate(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) - 1,
            cal.get(Calendar.DATE)
        )
        var d = cal.get(Calendar.DATE).toString()
        if (d.length == 1) {
            d = "0$d"
        }
        registerViewModel.dobDateLiveData.value = d

        var m = cal.get(Calendar.MONTH).toString()
        if (m.length == 1) {
            m = "0$m"
        }
        registerViewModel.dobMonthLiveData.value = m

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

                if (d != registerViewModel.dobDateLiveData.value) {
                    registerViewModel.dobDateLiveData.value = d
                }

                if (m != registerViewModel.dobMonthLiveData.value) {
                    registerViewModel.dobMonthLiveData.value = m
                }

                if (year.toString() != registerViewModel.dobYearLiveData.value) {
                    registerViewModel.dobYearLiveData.value = year.toString()
                }

                LogUtil.printObject("----->2: $d-$m-$year")
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

        registerViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as AuthenticationActivity).showHideProgressBar(it)
        }


        binding.btnUpdateProfile.setOnClickListener {
            registerViewModel.updateProfile()
        }

        registerViewModel.updateProfileResponse.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {

                    /* TrollaPreferencesManager.put(
                         it?.data?.data,
                         TrollaPreferencesManager.USER_DATA
                     )*/

                    (activity as AuthenticationActivity).addOrReplaceFragment(
                        MobileOTPVerificationFragment.getInstance(
                            registerViewModel.mobileNumberLiveData.value.toString()
                        ), true
                    )

                }
                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        binding.edtFirstname.requestFocus()

        return binding.root
    }

}