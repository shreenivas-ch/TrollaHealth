package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.HomeFragmentBinding
import com.trolla.healthsdk.feature_address.data.AddressListRefreshEvent
import com.trolla.healthsdk.feature_cart.presentation.CartFragment
import com.trolla.healthsdk.feature_categories.presentation.CategoriesFragment
import com.trolla.healthsdk.feature_dashboard.data.LoadAddressOnDashboardHeaderEvent
import com.trolla.healthsdk.utils.LogUtil
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import com.trolla.healthsdk.utils.hide
import com.trolla.healthsdk.utils.show
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.java.KoinJavaComponent.inject

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    val homeViewModel: HomeViewModel by inject(
        HomeViewModel::class.java
    )

    lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_fragment,
            container,
            false
        )

        //binding.viewModel = homeViewModel

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> {
                    binding.llLocationAndLogoHeader.show()
                    setCurrentFragment(DashboardFragment())
                }
                R.id.menuCategories -> {
                    binding.llLocationAndLogoHeader.hide()
                    setCurrentFragment(CategoriesFragment())
                }
                R.id.menuCart -> {
                    binding.llLocationAndLogoHeader.hide()
                    setCurrentFragment(CartFragment.newInstance(false))
                }
                R.id.menuSettings -> {
                    binding.llLocationAndLogoHeader.hide()
                    setCurrentFragment(ProfileFragment())
                }

            }
            true
        }

        fetchDefaultAddress()

        setCurrentFragment(DashboardFragment())

        return binding.root
    }

    fun fetchDefaultAddress() {
        if (TrollaPreferencesManager.get<String>(TrollaPreferencesManager.PM_DEFAULT_ADDRESS)
                .isNullOrEmpty()
        ) {
            binding.llLocationAndLogoHeader.hide()
        } else {
            binding.llLocationAndLogoHeader.show()
            binding.txtAddress.text =
                TrollaPreferencesManager.get<String>(TrollaPreferencesManager.PM_DEFAULT_ADDRESS)
                    .toString()
        }
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
    fun doThis(loadAddressOnDashboardHeaderEvent: LoadAddressOnDashboardHeaderEvent) {
        LogUtil.printObject("-----> loadAddressOnDashboardHeaderEvent event called")
        fetchDefaultAddress()
    }

    private fun setCurrentFragment(fragment: Fragment) {
        var transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.homeFramelayout, fragment)
        transaction.commit()
    }

}