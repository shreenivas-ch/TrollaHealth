package com.trolla.healthsdk.feature_dashboard.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.FragmentProfileBinding
import com.trolla.healthsdk.feature_address.presentation.AddressListFragment
import com.trolla.healthsdk.feature_auth.presentation.AuthenticationActivity
import com.trolla.healthsdk.feature_orders.presentation.OrdersListFragment
import com.trolla.healthsdk.ui_utils.WebviewActivity
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.TrollaPreferencesManager
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

        profileViewModel.profileLiveData.observe(viewLifecycleOwner)
        {
            binding.txtEmail.text = it?.email
            binding.txtUsername.text = it?.name
        }

        profileViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
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

        binding.rlCancellation.setOnClickListener {
            var intent = Intent(requireActivity(), WebviewActivity::class.java)
            intent.putExtra("title", "Cancellation Policy")
            intent.putExtra("url", "https://instastack.io/cancellation.html")
            startActivity(intent)
        }

        binding.rlRefundPolicy.setOnClickListener {
            var intent = Intent(requireActivity(), WebviewActivity::class.java)
            intent.putExtra("title", "Refund Policy")
            intent.putExtra("url", "https://instastack.io/refund.html")
            startActivity(intent)
        }

        binding.rlLogout.setOnClickListener {

            AlertDialog.Builder(requireActivity())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout ?")
                .setPositiveButton("Yes") { dialog, which ->

                    TrollaPreferencesManager.clearPreferences()
                    activity?.finish()
                    var myIntent = Intent(activity, AuthenticationActivity::class.java)
                    myIntent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    activity?.startActivity(myIntent)

                }
                .setNegativeButton("No", null)
                .show()
        }

        profileViewModel.getProfile()

        return binding.root
    }
}