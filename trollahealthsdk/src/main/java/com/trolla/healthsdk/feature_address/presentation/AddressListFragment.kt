package com.trolla.healthsdk.feature_address.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.databinding.AddressListFragmentBinding
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_cart.presentation.CartFragment
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsFragment
import org.koin.java.KoinJavaComponent.inject

class AddressListFragment : Fragment() {

    val action by lazy {
        arguments?.let {
            it.getString("action")
        }
    }

    companion object {
        fun newInstance(action: String): AddressListFragment {
            var bundle = Bundle().apply {
                putString("action", action)
            }
            var addressListFragment = AddressListFragment()
            addressListFragment.arguments = bundle
            return addressListFragment
        }
    }

    val addressListViewModel: AddressListViewModel by inject(
        AddressListViewModel::class.java
    )

    var addressList = ArrayList<ModelAddress>()
    lateinit var genericAdapter: GenericAdapter<ModelAddress>

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
        binding.lifecycleOwner = this

        genericAdapter = GenericAdapter(
            R.layout.item_address, addressList
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

            }

            override fun onDeleteAddressClick(view: View, position: Int) {

            }

            override fun onEditAddressClick(view: View, position: Int) {

            }
        })

        if (action == "select") {

        }

        binding.llAddNewAddress.setOnClickListener {
            var addNewAddressFragment = AddAddressFragment()
            (activity as DashboardActivity).addOrReplaceFragment(addNewAddressFragment, true)
        }

        binding.addressList.adapter = genericAdapter

        return binding.root
    }

}