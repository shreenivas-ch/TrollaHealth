package com.trolla.healthsdk.feature_dashboard.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.databinding.HomeFragmentBinding
import com.trolla.healthsdk.feature_address.presentation.AddAddressViewModel
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    val homeViewModel: HomeViewModel by inject(
        HomeViewModel::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<HomeFragmentBinding>(
            inflater,
            R.layout.home_fragment,
            container,
            false
        )

        //binding.viewModel = homeViewModel

        return binding.root
    }

}