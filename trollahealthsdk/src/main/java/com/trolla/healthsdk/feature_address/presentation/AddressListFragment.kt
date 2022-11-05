package com.trolla.healthsdk.feature_address.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.AddressListFragmentBinding
import com.trolla.healthsdk.feature_address.data.AddressListRefreshEvent
import com.trolla.healthsdk.feature_address.data.AddressSelectedEvent
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_dashboard.data.LoadAddressOnDashboardHeaderEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.java.KoinJavaComponent.inject

class AddressListFragment : Fragment() {

    val from by lazy {
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

    var tmpCount = 0
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
                if (from == "cart") {
                    EventBus.getDefault().post(AddressSelectedEvent(addressList[position]))
                    parentFragmentManager?.popBackStack()
                } else if (from == "home") {
                    (activity as DashboardActivity).saveDefaultAddressInPreferences(addressList[position])
                    EventBus.getDefault().post(LoadAddressOnDashboardHeaderEvent())
                    parentFragmentManager?.popBackStack()
                }
            }

            override fun onDeleteAddressClick(view: View, position: Int) {
                addressListViewModel.deleteAddress(addressList[position]._id)
            }

            override fun onEditAddressClick(view: View, position: Int) {
                var addAddressFragment =
                    AddAddressFragment.newInstance(addressList[position], from = from ?: "")
                (activity as DashboardActivity).addOrReplaceFragment(addAddressFragment, true)
            }
        })

        addressListViewModel.getaddressListLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    addressList.clear()
                    addressList.addAll(it.data?.data?.addresses!!)
                    for (i in addressList.indices) {
                        addressList[i].isSelect = from == "cart"
                    }

                    genericAdapter.notifyDataSetChanged()

                    if (addressList.size > 0) {
                        binding.llNoRecords.hide()
                    } else {
                        binding.llNoRecords.show()
                    }

                    if (tmpCount == 0) {
                        tmpCount = 1
                        if (addressList.size == 0 && from == "cart") {
                            parentFragmentManager.popBackStack()
                            var addNewAddressFragment =
                                AddAddressFragment.newInstance(from = from ?: "")
                            (activity as DashboardActivity).addOrReplaceFragment(
                                addNewAddressFragment,
                                true
                            )
                        }
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

        addressListViewModel.deleteAddressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    addressList.clear()
                    addressList.addAll(it.data?.data?.addresses!!)
                    for (i in addressList.indices) {
                        addressList[i].isSelect = from == "cart"
                    }

                    genericAdapter.notifyDataSetChanged()

                    var tmpAddresslist = it.data?.data?.addresses ?: arrayListOf()


                    var userDefaultAddress = ""
                    var userDefaultPincode = ""
                    if (tmpAddresslist.size > 0) {
                        userDefaultAddress = tmpAddresslist[0].address
                        userDefaultPincode = tmpAddresslist[0].pincode
                    }
                    TrollaPreferencesManager.setString(
                        userDefaultPincode,
                        TrollaPreferencesManager.PM_DEFAULT_PINCODE
                    )
                    TrollaPreferencesManager.setString(
                        userDefaultAddress,
                        TrollaPreferencesManager.PM_DEFAULT_ADDRESS
                    )

                    EventBus.getDefault().post(LoadAddressOnDashboardHeaderEvent())

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
            var addNewAddressFragment = AddAddressFragment.newInstance(from = from ?: "")
            (activity as DashboardActivity).addOrReplaceFragment(addNewAddressFragment, true)
        }

        addressListViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        binding.addressList.adapter = genericAdapter

        Handler(Looper.getMainLooper()).postDelayed({ addressListViewModel.getAddressList() }, 200)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(addressListRefreshEvent: AddressListRefreshEvent) {
        addressListViewModel.getAddressList()
    }

}