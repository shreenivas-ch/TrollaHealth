package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.badge.BadgeDrawable
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.HomeFragmentBinding
import com.trolla.healthsdk.feature_address.presentation.AddressListFragment
import com.trolla.healthsdk.feature_cart.data.models.AddToCartSuccessEvent
import com.trolla.healthsdk.feature_cart.data.models.CartCountChangeEvent
import com.trolla.healthsdk.feature_cart.data.models.CartDetailsRefreshedEvent
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
                    fetchDefaultAddress()
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

        (activity as DashboardActivity).cartViewModel.cartCountLiveData.value?.let {
            addBadge(it)
        }

        binding.llLocationAndLogoHeader.setOnClickListener {
            var addressListFragment = AddressListFragment.newInstance("home")
            (activity as DashboardActivity).addOrReplaceFragment(addressListFragment, true)
        }

        return binding.root
    }

    private fun addBadge(count: Int) {
        if (count == 0) {
            binding.bottomNavigationView?.removeBadge(R.id.menuCart)
        } else {
            val badge: BadgeDrawable = binding.bottomNavigationView.getOrCreateBadge(
                R.id.menuCart
            )
            badge.backgroundColor = ContextCompat.getColor(requireContext(), R.color.badgeColor)
            badge.number = count
            badge.isVisible = true
        }
    }

    fun fetchDefaultAddress() {
        if (TrollaPreferencesManager.getString(TrollaPreferencesManager.PM_DEFAULT_ADDRESS)
                .isNullOrEmpty()
        ) {
            binding.llLocationAndLogoHeader.hide()
        } else {
            binding.llLocationAndLogoHeader.show()
            binding.txtAddress.text =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PM_DEFAULT_ADDRESS)
                    ?.replaceFirstChar(Char::titlecase) + ", " + TrollaPreferencesManager.getString(
                    TrollaPreferencesManager.PM_DEFAULT_PINCODE
                )
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(cartDetailsRefreshedEvent: CartDetailsRefreshedEvent) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(addToCartSuccessEvent: AddToCartSuccessEvent) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(cartCountChangeEvent: CartCountChangeEvent) {
        addBadge(getCartViewModel().cartCountLiveData.value ?: 0)
    }

    private fun setCurrentFragment(fragment: Fragment) {
        var transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.homeFramelayout, fragment)
        transaction.commit()
    }

}