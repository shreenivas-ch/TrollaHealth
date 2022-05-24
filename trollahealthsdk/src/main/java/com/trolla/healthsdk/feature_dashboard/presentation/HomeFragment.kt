package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.HomeFragmentBinding
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
                R.id.menuHome -> setCurrentFragment(DashboardFragment())
                R.id.menuProfile -> setCurrentFragment(ProfileFragment())
                R.id.menuCart -> setCurrentFragment(CartFragment())
                R.id.menuSettings -> setCurrentFragment(SettingsFragment())

            }
            true
        }

        return binding.root
    }

    private fun setCurrentFragment(fragment: Fragment) {
        var transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.homeFramelayout, fragment)
        transaction.commit()
    }

}