package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentSettingsBinding
import org.koin.java.KoinJavaComponent.inject

class SettingsFragment : Fragment() {

    val settingsViewModel: SettingsViewModel by inject(SettingsViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<FragmentSettingsBinding>(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = settingsViewModel

        settingsViewModel.headerTitle.value = "Settings"
        settingsViewModel.headerBottomLine.value = 1
        settingsViewModel.headerBackButton.value = 0

        return binding.root
    }
}