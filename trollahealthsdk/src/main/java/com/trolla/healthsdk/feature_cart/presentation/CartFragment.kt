package com.trolla.healthsdk.feature_cart.presentation

import android.media.metrics.Event
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.AddAddressFragmentBinding
import com.trolla.healthsdk.databinding.CartFragmentBinding
import com.trolla.healthsdk.feature_address.presentation.AddAddressViewModel
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_dashboard.data.AddToCartActionEvent
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.RefreshDashboardEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_dashboard.presentation.HomeFragment
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsFragment
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import org.greenrobot.eventbus.EventBus
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class CartFragment : Fragment() {

    companion object {
        fun newInstance() = CartFragment()
    }

    var cartItemsList = ArrayList<GetCartDetailsResponse.CartProduct>()
    lateinit var genericAdapter: GenericAdapter<GetCartDetailsResponse.CartProduct>

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

        genericAdapter = GenericAdapter<GetCartDetailsResponse.CartProduct>(
            R.layout.item_cart_product,
            cartItemsList
        )

        genericAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

            }

            override fun onAddToCartClick(view: View, position: Int) {


            }

            override fun goToCart() {

            }

            override fun cartMinusClick(view: View, position: Int) {
                val response =
                    (activity as DashboardActivity).cartViewModel.cartDetailsResponseLiveData.value
                var productid = response?.data?.data?.products?.get(position)?.product?.product_id
                var existingQty = response?.data?.data?.products?.get(position)?.qty
                var newQty = existingQty!! - 1
                EventBus.getDefault().post(AddToCartActionEvent(productid!!, newQty))

            }

            override fun cartPlusClick(view: View, position: Int) {
                val response =
                    (activity as DashboardActivity).cartViewModel.cartDetailsResponseLiveData.value
                var productid = response?.data?.data?.products?.get(position)?.product?.product_id
                var existingQty = response?.data?.data?.products?.get(position)?.qty
                var newQty = existingQty!! + 1
                EventBus.getDefault().post(AddToCartActionEvent(productid!!, newQty))
            }
        })

        binding.cartList.adapter = genericAdapter

        (activity as DashboardActivity).cartViewModel.addToCartResponseLiveData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Resource.Success -> {
                    (activity as DashboardActivity).cartViewModel.getCartDetails()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        (activity as DashboardActivity).cartViewModel.cartDetailsResponseLiveData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Resource.Success -> {
                    val response = it.data?.data?.products
                    cartItemsList.clear()
                    cartItemsList.addAll(response!!)
                    //genericAdapter.addItems(response!!)
                    genericAdapter.notifyDataSetChanged()
                    binding.executePendingBindings()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        (activity as DashboardActivity).cartViewModel.getCartDetails()

        return binding.root
    }

}