package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.FragmentProfileBinding
import com.trolla.healthsdk.feature_address.presentation.AddressListFragment
import com.trolla.healthsdk.feature_orders.presentation.OrdersListFragment
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import org.koin.java.KoinJavaComponent.inject

class ProfileFragment : Fragment() {

    val profileViewModel: ProfileViewModel by inject(ProfileViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = profileViewModel

        profileViewModel.headerTitle.value = "My Profile"
        profileViewModel.headerBottomLine.value = 1
        profileViewModel.headerBackButton.value = 0

        profileViewModel.getProfileResponseLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {
                    binding.txtEmail.text = it?.data?.data?.email
                    binding.txtUsername.text = it?.data?.data?.name
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        profileViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        binding.txtMyOrders.setOnClickListener {
            (activity as DashboardActivity).addOrReplaceFragment(
                OrdersListFragment.newInstance(),
                true
            )
        }

        binding.txtMyAddresses.setOnClickListener {
            (activity as DashboardActivity).addOrReplaceFragment(
                AddressListFragment.newInstance("profile"),
                true
            )
        }

        profileViewModel.getProfile()

        return binding.root
    }
}