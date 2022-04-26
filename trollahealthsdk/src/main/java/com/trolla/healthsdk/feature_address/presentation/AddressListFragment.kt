package com.trolla.healthsdk.feature_address.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.databinding.AddressListFragmentBinding
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class AddressListFragment : Fragment() {

    companion object {
        fun newInstance() = AddressListFragment()
    }

    val addressListViewModel: AddressListViewModel by inject(
        AddressListViewModel::class.java
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<AddressListFragmentBinding>(
            inflater,
            R.layout.address_list_fragment,
            container,
            false
        )

        binding.viewModel = addressListViewModel

        return binding.root
    }

}