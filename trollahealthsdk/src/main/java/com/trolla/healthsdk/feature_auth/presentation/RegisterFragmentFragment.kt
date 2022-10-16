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
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.utils.*
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class RegisterFragmentFragment : Fragment() {

    companion object {
        fun getInstance(from: String): RegisterFragmentFragment {
            var bundle = Bundle()
            bundle.putString("from", from)
            var registerFragmentFragment = RegisterFragmentFragment()
            registerFragmentFragment.arguments = bundle
            return registerFragmentFragment
        }
    }

    val from by lazy {
        arguments?.let {
            it.getString("from")
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

        registerViewModel.headerTitle.value = if (from == "auth") "Register" else "Edit Profile"

        binding.edtMobileNumber.isEnabled =
            TrollaPreferencesManager.getBoolean(TrollaPreferencesManager.IS_PROFILE_COMPLETE) != true

        if (from == "profile") {
            registerViewModel.getProfile()
            binding.btnUpdateProfile.text = "Update Profile"
            binding.txtSkip.hide()
        } else {
            binding.txtSkip.show()
        }

        binding.txtSkip.setOnClickListener {
            (activity as DashboardActivity).removeAllFragmentFromDashboardBackstack()
            ((activity as DashboardActivity)).init = false
            (activity as DashboardActivity).getAddressListOnDashboard()
        }

        registerViewModel.profileNameLiveData.observe(viewLifecycleOwner)
        {
            var namearr = it?.split(" ") ?: arrayListOf()

            if ((namearr.size) > 0) {
                binding.edtFirstname.setText(namearr[0] ?: "")
            }

            if (namearr.size == 2) {
                binding.edtLastname.setText(namearr[1])
            }

            if ((namearr.size) > 2) {
                var lastname = ""
                for (i in namearr.indices) {
                    if (i != 0) {
                        lastname += namearr[i] +" "
                    }
                }
                binding.edtLastname.setText(lastname.trim())
            }
        }

        registerViewModel.profileMobileLiveData.observe(viewLifecycleOwner)
        {
            binding.edtMobileNumber.setText(it)
        }

        registerViewModel.profileGenderLiveData.observe(viewLifecycleOwner)
        {
            registerViewModel.genderLiveData.value = it
            if (it == registerViewModel.genderMaleConstant) {
                binding.radioMale.isChecked = true
                binding.radioFemale.isChecked = false
            } else if (it == registerViewModel.genderFemaleConstant) {
                binding.radioMale.isChecked = false
                binding.radioFemale.isChecked = true
            }
        }

        registerViewModel.profileDOBLiveData.observe(viewLifecycleOwner)
        {
            var cal = Calendar.getInstance()
            cal.time = it
            binding.datePicker.updateDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) - 1,
                cal.get(Calendar.DATE)
            )
        }

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
            (activity as DashboardActivity).showHideProgressBar(it)
        }


        binding.btnUpdateProfile.setOnClickListener {
            registerViewModel.updateProfile()
        }

        registerViewModel.updateProfileResponse.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {

                    if (from == "profile") {
                        activity?.showLongToast("Profile Updated")

                        TrollaPreferencesManager.setString(
                            it?.data?.data?.userData?._id,
                            TrollaPreferencesManager.PROFILE_ID
                        )
                        TrollaPreferencesManager.setString(
                            it?.data?.data?.userData?.name,
                            TrollaPreferencesManager.PROFILE_NAME
                        )
                        TrollaPreferencesManager.setString(
                            it?.data?.data?.userData?.email,
                            TrollaPreferencesManager.PROFILE_EMAIL
                        )
                        TrollaPreferencesManager.setString(
                            it?.data?.data?.userData?.mobile,
                            TrollaPreferencesManager.PROFILE_MOBILE
                        )
                        TrollaPreferencesManager.setString(
                            it?.data?.data?.userData?.gender,
                            TrollaPreferencesManager.PROFILE_GENDER
                        )
                        TrollaPreferencesManager.setString(
                            it?.data?.data?.userData?.day,
                            TrollaPreferencesManager.PROFILE_DAY
                        )
                        TrollaPreferencesManager.setString(
                            it?.data?.data?.userData?.month,
                            TrollaPreferencesManager.PROFILE_MONTH
                        )
                        TrollaPreferencesManager.setString(
                            it?.data?.data?.userData?.year,
                            TrollaPreferencesManager.PROFILE_YEAR
                        )

                        if (TrollaPreferencesManager.getBoolean(TrollaPreferencesManager.IS_PROFILE_COMPLETE) == true) {
                            parentFragmentManager?.popBackStack()
                        } else {
                            (activity as DashboardActivity).addOrReplaceFragment(
                                MobileOTPVerificationFragment.getInstance(
                                    registerViewModel.mobileNumberLiveData.value.toString()
                                ), true
                            )
                        }

                    } else {
                        (activity as DashboardActivity).addOrReplaceFragment(
                            MobileOTPVerificationFragment.getInstance(
                                registerViewModel.mobileNumberLiveData.value.toString()
                            ), true
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

        binding.edtFirstname.requestFocus()

        return binding.root
    }

}