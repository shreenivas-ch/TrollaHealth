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
import com.trolla.healthsdk.feature_address.data.AddressSelectedEvent
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_address.presentation.AddressListFragment
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.data.models.RefreshCartEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_prescriptionupload.data.ModelPrescription
import com.trolla.healthsdk.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.java.KoinJavaComponent.inject

class CartFragment : Fragment() {

    companion object {
        fun newInstance(showBackButton: Boolean = true): CartFragment {
            var bundle = Bundle()
            bundle.putBoolean("showBackButton", showBackButton)
            var cartFragment = CartFragment()
            cartFragment.arguments = bundle
            return cartFragment
        }
    }

    val showBackButton by lazy {
        arguments?.let {
            it.getBoolean("showBackButton")
        }
    }

    var cartItemsListWithoutRx = ArrayList<GetCartDetailsResponse.CartProduct>()
    var cartItemsListWithRx = ArrayList<GetCartDetailsResponse.CartProduct>()
    var uploadedPrescriptionsList = ArrayList<ModelPrescription>()
    lateinit var cartAdapterWithoutRx: GenericAdapter<GetCartDetailsResponse.CartProduct>
    lateinit var cartAdapterWithRx: GenericAdapter<GetCartDetailsResponse.CartProduct>
    lateinit var cartUploadedPrescriptionsAdapter: GenericAdapter<ModelPrescription>

    val cartViewModel: CartViewModel by inject(CartViewModel::class.java)
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

        showBackButton?.let {
            cartViewModel.headerBackButton.value = if (it) 1 else 0
        }

        cartAdapterWithoutRx = GenericAdapter(
            R.layout.item_cart_product,
            cartItemsListWithoutRx
        )

        cartAdapterWithRx = GenericAdapter(
            R.layout.item_cart_product,
            cartItemsListWithRx
        )

        cartUploadedPrescriptionsAdapter = GenericAdapter(
            R.layout.item_uploaded_prescription,
            uploadedPrescriptionsList
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

        cartUploadedPrescriptionsAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {

            }

            override fun onDeletePrescriptionClick(view: View, position: Int) {
                var newarray = ArrayList<String>()
                for (i in uploadedPrescriptionsList.indices) {
                    if (i != position) {
                        newarray.add(uploadedPrescriptionsList[i].url)
                    }
                }

                cartViewModel.addToCart(0, 0, TrollaConstants.ADDTOCART_TYPE_PRESCRIPTION, newarray)
            }

        })

        binding.cartList.adapter = cartAdapterWithoutRx
        binding.cartListWithRequiredPrescription.adapter = cartAdapterWithRx
        binding.rvUploadedPrescriptions.adapter = cartUploadedPrescriptionsAdapter

        cartViewModel.addToCartResponseLiveData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Resource.Success -> {

                    processCartData(
                        it.data?.data?.cart?.products!!,
                        it?.data?.data?.cart?.prescriptions
                    )
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

                    processCartData(
                        it.data?.data?.products!!,
                        it.data?.data?.prescriptions
                    )

                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        cartViewModel.createOrderResponseLiveData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Resource.Success -> {
                    parentFragmentManager?.popBackStack()

                    (activity as DashboardActivity).addOrReplaceFragment(
                        OrderConfirmedFragment.newInstance(
                            it?.data?.data?.order?.order_id ?: "",
                            cartViewModel.selectedPaymentModeLiveData.value,
                            it?.data?.data?.transaction?._id,
                            it?.data?.data?.order?.amount
                        ),
                        true
                    )
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

        binding.rlCashonDelivery.setOnClickListener {
            cartViewModel.selectedPaymentModeLiveData.value = "COD"
            binding.imgCOD.setImageResource(R.drawable.ic_cod)
            binding.imgOnline.setImageResource(R.drawable.ic_payonline_inactive)
            binding.imgCODSelected.setImageResource(R.drawable.ic_selected)
            binding.imgOnlineSelected.setImageResource(R.drawable.ic_unselected)
        }
        binding.rlPayOnline.setOnClickListener {
            cartViewModel.selectedPaymentModeLiveData.value = "prepaid"
            binding.imgCOD.setImageResource(R.drawable.ic_cod_inactive)
            binding.imgOnline.setImageResource(R.drawable.ic_payonline)
            binding.imgCODSelected.setImageResource(R.drawable.ic_unselected)
            binding.imgOnlineSelected.setImageResource(R.drawable.ic_selected)
        }

        binding.llAddressNotAdded.setOnClickListener {
            var addressListFragment = AddressListFragment.newInstance("cart")
            (activity as DashboardActivity).addOrReplaceFragment(addressListFragment, true)
        }

        binding.txtChangeAddress.setOnClickListener {
            var addressListFragment = AddressListFragment.newInstance("cart")
            (activity as DashboardActivity).addOrReplaceFragment(addressListFragment, true)
        }

        cartViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        cartViewModel.selectedAddressIdLiveData.observe(viewLifecycleOwner)
        {
            checkIfCartValid()
        }

        cartViewModel.selectedPaymentModeLiveData.observe(viewLifecycleOwner)
        {
            checkIfCartValid()
        }

        return binding.root
    }

    fun processCartData(
        products: ArrayList<GetCartDetailsResponse.CartProduct>,
        prescriptionsList: ArrayList<String>
    ) {
        cartItemsListWithoutRx.clear()
        cartItemsListWithRx.clear()
        uploadedPrescriptionsList.clear()

        for (i in products!!.indices) {
            if (products[i].product.rx_type == "NON-RX" || products[i].product.rx_type == "NON-RX") {
                cartItemsListWithRx.add(products[i])
            } else {
                cartItemsListWithRx.add(products[i])
            }
            LogUtil.printObject("----->: " + products[i].qty)
        }

        for (i in prescriptionsList.indices) {
            uploadedPrescriptionsList.add(ModelPrescription(prescriptionsList[i]))
        }

        cartAdapterWithoutRx.notifyDataSetChanged()
        cartAdapterWithRx.notifyDataSetChanged()
        cartUploadedPrescriptionsAdapter.notifyDataSetChanged()

        binding.llPrescriptionLayout.setVisibilityOnBoolean(cartItemsListWithRx.size != 0, true)

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
        binding.rlSelectedDeliveryAddress.setVisibilityOnBoolean(
            cartViewModel.selectedAddressIdLiveData.value == "",
            false
        )
        binding.llAddressNotAdded.setVisibilityOnBoolean(
            cartViewModel.selectedAddressIdLiveData.value == "",
            true
        )

        if (!prescriptionsList.isNullOrEmpty() && cartItemsListWithRx.size > 0) {
            binding.txtAddNewPrescription.show()
        } else {
            binding.txtAddNewPrescription.hide()
        }

        binding.rvUploadedPrescriptions.setVisibilityOnBoolean(
            prescriptionsList == null || prescriptionsList.size == 0,
            false
        )

        if (prescriptionsList.isNullOrEmpty() && cartItemsListWithRx.size > 0) {
            binding.llUploadOptions.show()
        } else {
            binding.llUploadOptions.hide()
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
    fun doThis(address: RefreshCartEvent) {
        cartViewModel.getCartDetails()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(address: AddressSelectedEvent) {

        cartViewModel.selectedAddressIdLiveData.value = address.modelAddress._id

        binding.rlSelectedDeliveryAddress.setVisibilityOnBoolean(
            address.modelAddress == null,
            false
        )
        binding.llAddressNotAdded.setVisibilityOnBoolean(address.modelAddress == null, true)

        binding.txtAddressType.text =
            (address.modelAddress?.type ?: "Home").replaceFirstChar { it.uppercase() }
        binding.txtSelectedAddress.text =
            address.modelAddress.name + "\n" + address.modelAddress.address + " " + address.modelAddress.landmark + " " + address.modelAddress.city + " " + address.modelAddress.state + "\n" + address.modelAddress.pincode

    }

    fun checkIfCartValid() {
        cartViewModel.isCartValid.value =
            cartViewModel.selectedPaymentModeLiveData.value != "" && cartViewModel.selectedAddressIdLiveData.value != ""
    }

}