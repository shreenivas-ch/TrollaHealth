package com.trolla.healthsdk.feature_cart.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.CartFragmentBinding
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.utils.LogUtil
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import com.trolla.healthsdk.utils.setVisibilityOnBoolean
import org.koin.java.KoinJavaComponent

class CartFragment : Fragment() {

    companion object {
        fun newInstance() = CartFragment()
    }

    var cartItemsListWithoutRx = ArrayList<GetCartDetailsResponse.CartProduct>()
    var cartItemsListWithRx = ArrayList<GetCartDetailsResponse.CartProduct>()
    lateinit var cartAdapterWithoutRx: GenericAdapter<GetCartDetailsResponse.CartProduct>
    lateinit var cartAdapterWithRx: GenericAdapter<GetCartDetailsResponse.CartProduct>

    val cartViewModel: CartViewModel by KoinJavaComponent.inject(CartViewModel::class.java)
    lateinit var binding: CartFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.cart_fragment,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = cartViewModel

        cartViewModel.headerTitle.value = "My Cart"
        cartViewModel.headerBottomLine.value = 1

        cartAdapterWithoutRx = GenericAdapter(
            R.layout.item_cart_product,
            cartItemsListWithoutRx
        )

        cartAdapterWithRx = GenericAdapter(
            R.layout.item_cart_product,
            cartItemsListWithRx
        )

        cartAdapterWithoutRx.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

            }

            override fun onAddToCartClick(view: View, position: Int) {


            }

            override fun goToCart() {

            }

            override fun cartMinusClick(view: View, position: Int) {
                val product = cartItemsListWithoutRx[position]
                var productid = product.product.product_id
                var existingQty = product.qty
                var newQty = existingQty!! - 1
                cartViewModel.addToCart(productid!!, newQty)

            }

            override fun cartPlusClick(view: View, position: Int) {
                val product = cartItemsListWithoutRx[position]
                var productid = product.product.product_id
                var existingQty = product.qty
                var newQty = existingQty!! + 1
                cartViewModel.addToCart(productid!!, newQty)
            }

            override fun cartDeleteClick(view: View, position: Int) {
                val product = cartItemsListWithoutRx[position]
                var productid = product.product.product_id
                cartViewModel.addToCart(productid!!, 0)
            }
        })

        cartAdapterWithRx.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

            }

            override fun onAddToCartClick(view: View, position: Int) {


            }

            override fun goToCart() {

            }

            override fun cartMinusClick(view: View, position: Int) {
                val product = cartItemsListWithRx[position]
                var productid = product.product.product_id
                var existingQty = product.qty
                var newQty = existingQty!! - 1
                cartViewModel.addToCart(productid!!, newQty)

            }

            override fun cartPlusClick(view: View, position: Int) {
                val product = cartItemsListWithRx[position]
                var productid = product.product.product_id
                var existingQty = product.qty
                var newQty = existingQty!! + 1
                cartViewModel.addToCart(productid!!, newQty)
            }

            override fun cartDeleteClick(view: View, position: Int) {
                val product = cartItemsListWithRx[position]
                var productid = product.product.product_id
                cartViewModel.addToCart(productid!!, 0)
            }
        })

        binding.cartList.adapter = cartAdapterWithoutRx
        binding.cartListWithRequiredPrescription.adapter = cartAdapterWithRx

        cartViewModel.addToCartResponseLiveData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Resource.Success -> {

                    processCartData(it.data?.data?.cart?.products!!)
                    //(activity as DashboardActivity).cartViewModel.getCartDetails()
                    (activity as DashboardActivity).cartViewModel.addToCartResponseLiveData.value =
                        it
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        cartViewModel.cartDetailsResponseLiveData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Resource.Success -> {

                    LogUtil.printObject("-----> CartFragment: CartDetails")

                    processCartData(it.data?.data?.products!!)

                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        cartViewModel.getCartDetails()

        return binding.root
    }

    fun processCartData(products: ArrayList<GetCartDetailsResponse.CartProduct>) {
        cartItemsListWithoutRx.clear()
        cartItemsListWithRx.clear()

        for (i in products!!.indices) {
            if (products[i].product.rx_type == "NON-RX" || products[i].product.rx_type == "NON-RX") {
                cartItemsListWithoutRx.add(products[i])
            } else {
                cartItemsListWithRx.add(products[i])
            }

            LogUtil.printObject("----->: " + products[i].qty)
        }

        cartAdapterWithoutRx.notifyDataSetChanged()
        cartAdapterWithRx.notifyDataSetChanged()

        binding.txtLabelPrescriptionNotRequired.setVisibilityOnBoolean(
            cartItemsListWithoutRx.size == 0,
            false
        )
        binding.txtLabelPrescriptionRequired.setVisibilityOnBoolean(
            cartItemsListWithRx.size == 0,
            false
        )

        binding.cartNesterScrollView.setVisibilityOnBoolean(products.size == 0, false)
        binding.cardViewCartPayment.setVisibilityOnBoolean(products.size == 0, false)
        binding.txtCartEmpty.setVisibilityOnBoolean(products.size == 0, true)
        binding.rlSelectedDeliveryAddress.setVisibilityOnBoolean(products.size == 0, true)
    }

}