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
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.AddressListFragmentBinding
import com.trolla.healthsdk.feature_address.data.AddressSelectedEvent
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.utils.LogUtil
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import org.greenrobot.eventbus.EventBus
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

        addressListViewModel.headerTitle.value = "Addresses"
        addressListViewModel.headerBottomLine.value = 1

        genericAdapter = GenericAdapter(
            R.layout.item_address, addressList
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                if (action == "cart") {
                    EventBus.getDefault().post(AddressSelectedEvent(addressList[position]))
                    parentFragmentManager?.popBackStack()
                }
            }

            override fun onDeleteAddressClick(view: View, position: Int) {
                addressListViewModel.deleteAddress(addressList.get(position)._id)
            }

            override fun onEditAddressClick(view: View, position: Int) {
                var addAddressFragment = AddAddressFragment.newInstance(addressList[position])
                (activity as DashboardActivity).addOrReplaceFragment(addAddressFragment, true)
            }
        })

        addressListViewModel.getaddressListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    addressList.clear()
                    addressList.addAll(it.data?.data?.addresses!!)
                    for (i in addressList.indices) {
                        addressList[i].isSelect = action == "cart"
                    }

                    genericAdapter.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        addressListViewModel.addAddressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {

                    addressList.clear()
                    addressList.addAll(it.data?.data?.addresses!!)
                    for (i in addressList.indices) {
                        addressList[i].isSelect = action != "cart"
                    }

                    genericAdapter.notifyDataSetChanged()

                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        addressListViewModel.deleteAddressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    addressList.clear()
                    addressList.addAll(it.data?.data?.addresses!!)
                    for (i in addressList.indices) {
                        addressList[i].isSelect = action != "cart"
                    }

                    genericAdapter.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        addressListViewModel.updateAddressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    addressList.clear()
                    addressList.addAll(it.data?.data?.addresses!!)
                    for (i in addressList.indices) {
                        addressList[i].isSelect = action != "cart"
                    }

                    genericAdapter.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        binding.llAddNewAddress.setOnClickListener {
            var addNewAddressFragment = AddAddressFragment()
            (activity as DashboardActivity).addOrReplaceFragment(addNewAddressFragment, true)
        }

        binding.addressList.adapter = genericAdapter

        addressListViewModel.getAddressList()

        return binding.root
    }

}