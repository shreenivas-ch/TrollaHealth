package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentProfileBinding
import com.trolla.healthsdk.feature_orders.presentation.OrdersListFragment
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

        binding.txtMyOrders.setOnClickListener {
            (activity as DashboardActivity).addOrReplaceFragment(
                OrdersListFragment.newInstance(),
                true
            )
        }

        return binding.root
    }
}