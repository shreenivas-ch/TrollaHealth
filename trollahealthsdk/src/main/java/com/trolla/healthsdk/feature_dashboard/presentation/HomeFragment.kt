package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.HomeFragmentBinding
import com.trolla.healthsdk.feature_cart.presentation.CartFragment
import com.trolla.healthsdk.utils.hide
import com.trolla.healthsdk.utils.show
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

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> {
                    binding.imgVendorLogo.show()
                    setCurrentFragment(DashboardFragment())
                }
                R.id.menuProfile -> {
                    binding.imgVendorLogo.hide()
                    setCurrentFragment(ProfileFragment())
                }
                R.id.menuCart -> {
                    binding.imgVendorLogo.hide()
                    setCurrentFragment(CartFragment.newInstance(false))
                }
                R.id.menuSettings -> {
                    binding.imgVendorLogo.hide()
                    setCurrentFragment(SettingsFragment())
                }

            }
            true
        }

        setCurrentFragment(DashboardFragment())

        return binding.root
    }

    private fun setCurrentFragment(fragment: Fragment) {
        var transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.homeFramelayout, fragment)
        transaction.commit()
    }

}