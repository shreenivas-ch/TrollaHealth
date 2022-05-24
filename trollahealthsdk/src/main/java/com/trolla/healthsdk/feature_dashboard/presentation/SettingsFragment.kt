package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentSettingsBinding

class SettingsFragment:Fragment() {
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

        return binding.root
    }
}