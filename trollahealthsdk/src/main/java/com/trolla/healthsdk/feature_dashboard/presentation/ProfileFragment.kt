package com.trolla.healthsdk.feature_dashboard.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.freshchat.consumer.sdk.Freshchat
import com.freshchat.consumer.sdk.FreshchatConfig
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentProfileBinding
import com.trolla.healthsdk.feature_address.presentation.AddressListFragment
import com.trolla.healthsdk.feature_auth.presentation.LoginEmailFragment
import com.trolla.healthsdk.feature_auth.presentation.RegisterFragmentFragment
import com.trolla.healthsdk.feature_orders.presentation.OrdersListFragment
import com.trolla.healthsdk.ui_utils.WebviewActivity
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import org.koin.java.KoinJavaComponent.inject


class ProfileFragment : Fragment() {

    val profileViewModel: ProfileViewModel by inject(ProfileViewModel::class.java)

    val isProfileComplete by lazy {
        TrollaPreferencesManager?.getString(TrollaPreferencesManager.IS_PROFILE_COMPLETE)
    }

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

        profileViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        binding.rlEditProfile.setOnClickListener {
            (activity as DashboardActivity).addOrReplaceFragment(
                RegisterFragmentFragment.getInstance("profile"),
                true
            )
        }

        binding.rlMyOrders.setOnClickListener {
            (activity as DashboardActivity).addOrReplaceFragment(
                OrdersListFragment.newInstance(),
                true
            )
        }

        binding.rlAddressBook.setOnClickListener {
            (activity as DashboardActivity).addOrReplaceFragment(
                AddressListFragment.newInstance("profile"),
                true
            )
        }

        binding.rlPrivacyPolicy.setOnClickListener {
            var intent = Intent(requireActivity(), WebviewActivity::class.java)
            intent.putExtra("title", "Privacy Policy")
            intent.putExtra("url", "https://instastack.io/privacy.html")
            startActivity(intent)
        }

        binding.rlTerms.setOnClickListener {
            var intent = Intent(requireActivity(), WebviewActivity::class.java)
            intent.putExtra("title", "Terms & Conditions")
            intent.putExtra("url", "https://instastack.io/terms.html")
            startActivity(intent)
        }

        binding.rlRefundPolicy.setOnClickListener {
            var intent = Intent(requireActivity(), WebviewActivity::class.java)
            intent.putExtra("title", "Refund & Cancellation policy")
            intent.putExtra("url", "https://instastack.io/refund.html")
            startActivity(intent)
        }

        binding.rlChatSupport.setOnClickListener {
            initiateChatSupport()
        }

        binding.rlLogout.setOnClickListener {

            AlertDialog.Builder(requireActivity())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout ?")
                .setPositiveButton("Yes") { dialog, which ->

                    TrollaPreferencesManager.clearPreferences()
                    (activity as DashboardActivity).removeAllFragmentFromDashboardBackstack()
                    ((activity as DashboardActivity)).init = false
                    (activity as DashboardActivity).addOrReplaceFragment(LoginEmailFragment())

                }
                .setNegativeButton("No", null)
                .show()
        }

        profileViewModel.getProfile()
        if (TrollaPreferencesManager.getBoolean(TrollaPreferencesManager.IS_PROFILE_COMPLETE) == true) {
            binding.txtUsername.text = profileViewModel.profileNameLiveData.value
            binding.txtEmail.text = profileViewModel.profileEmailLiveData.value
        } else {
            binding.txtUsername.text = "Update your profile".uppercase()
            binding.txtEmail.text = profileViewModel.profileEmailLiveData.value
        }

        return binding.root
    }
}