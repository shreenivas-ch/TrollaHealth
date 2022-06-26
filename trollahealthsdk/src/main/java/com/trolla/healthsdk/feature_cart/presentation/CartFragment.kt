package com.trolla.healthsdk.feature_cart.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.databinding.CartFragmentBinding
import com.trolla.healthsdk.feature_address.presentation.AddAddressViewModel
import com.trolla.healthsdk.feature_dashboard.data.RefreshDashboardEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_dashboard.presentation.HomeFragment
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import org.greenrobot.eventbus.EventBus
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class CartFragment : Fragment() {

    companion object {
        fun newInstance() = CartFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var binding = DataBindingUtil.inflate<CartFragmentBinding>(
            inflater,
            R.layout.cart_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this

        (activity as DashboardActivity).cartViewModel.addToCartResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {

                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString( requireContext())
                    )
                }
            }
        }

        (activity as DashboardActivity).cartViewModel.cartDetailsResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    val response = it.data?.data?.products
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString( requireContext())
                    )
                }
            }
        }

        (activity as DashboardActivity).cartViewModel.getCartDetails()

        return binding.root
    }

}