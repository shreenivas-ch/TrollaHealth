package com.trolla.healthsdk.feature_address.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.feature_address.data.AddressListRefreshEvent
import com.trolla.healthsdk.feature_address.data.AddressSelectedEvent
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import com.trolla.healthsdk.utils.hide
import com.trolla.healthsdk.utils.show
import org.greenrobot.eventbus.EventBus
import org.koin.java.KoinJavaComponent.inject

class AddAddressFragment : Fragment() {

    companion object {
        fun newInstance(modelAddress: ModelAddress? = null, from: String): AddAddressFragment {
            var bundle = Bundle()
            bundle.putParcelable("addressToEdit", modelAddress)
            bundle.putString("from", from)
            var addAddressFragment = AddAddressFragment()
            addAddressFragment.arguments = bundle
            return addAddressFragment
        }
    }

    val addAddressViewModel: AddAddressViewModel by inject(
        AddAddressViewModel::class.java
    )

    val from by lazy {
        arguments?.let {
            it.getString("from")
        }
    }

    val addressToEdit by lazy {
        arguments?.let {
            it.getParcelable<ModelAddress>("addressToEdit")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<AddAddressFragmentBinding>(
            inflater,
            R.layout.add_address_fragment,
            container,
            false
        )

        binding.viewModel = addAddressViewModel
        binding.lifecycleOwner = this

        addAddressViewModel.headerTitle.value = "Add Address"
        addAddressViewModel.headerBackButton.value = 1
        addAddressViewModel.headerBottomLine.value = 1

        addressToEdit?.let {
            addAddressViewModel.addressIdLiveData.value = it?._id
            addAddressViewModel.addressNameLiveData.value = it?.name
            addAddressViewModel.addressContactLiveData.value = it?.contact
            addAddressViewModel.addressLiveData.value = it?.address
            addAddressViewModel.addressCityLiveData.value = it?.city
            addAddressViewModel.addressStateLiveData.value = it?.state
            addAddressViewModel.addressLandmarkLiveData.value = it?.landmark
            addAddressViewModel.addressPincodeLiveData.value = it?.pincode
            addAddressViewModel.addressTypeLiveData.value = it?.type
            addAddressViewModel.addressOtheriveData.value = it?.other ?: ""

            if (it.type.lowercase() == "home") {
                binding.radioHome.isSelected = true
            } else if (it.type.lowercase() == "work") {
                binding.radioWork.isSelected = true
            } else {
                binding.radioOther.isSelected = true
            }
        }

        binding.radioHome.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                addAddressViewModel.addressTypeLiveData.value =
                    addAddressViewModel.addressTypeHomeConstant
                addAddressViewModel.addressOtheriveData.value = ""
                binding.inputOtherLocation.hide()
            }
        }

        binding.radioWork.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                addAddressViewModel.addressTypeLiveData.value =
                    addAddressViewModel.addressTypeWorkConstant
                addAddressViewModel.addressOtheriveData.value = ""
                binding.inputOtherLocation.hide()
            }
        }
        binding.radioOther.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                addAddressViewModel.addressTypeLiveData.value =
                    addAddressViewModel.addressTypeOtherConstant
                addAddressViewModel.addressOtheriveData.value = ""
                binding.inputOtherLocation.show()
            }
        }

        addAddressViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        addAddressViewModel.addAddressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {

                    if (from == "cart") {
                        EventBus.getDefault().post(AddressSelectedEvent(it?.data?.data?.address!!))
                    }
                    else
                    {
                        EventBus.getDefault().post(AddressListRefreshEvent())
                    }
                    parentFragmentManager.popBackStack()

                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        addAddressViewModel.updateAddressLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    parentFragmentManager.popBackStack()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        return binding.root
    }
}