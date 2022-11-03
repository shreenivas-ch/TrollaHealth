package com.trolla.healthsdk.feature_cart.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.CartFragmentBinding
import com.trolla.healthsdk.feature_address.data.AddressSelectedEvent
import com.trolla.healthsdk.feature_address.presentation.AddressListFragment
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.data.models.AddToCartSuccessEvent
import com.trolla.healthsdk.feature_cart.data.models.CartDetailsRefreshedEvent
import com.trolla.healthsdk.feature_cart.data.models.PrescriptionUploadedEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_dashboard.presentation.getCartViewModel
import com.trolla.healthsdk.feature_prescriptionupload.data.ModelPrescription
import com.trolla.healthsdk.feature_productdetails.presentation.FullscreenImageViewerActivity
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsFragment
import com.trolla.healthsdk.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        binding.viewModel = getCartViewModel()

        getCartViewModel().headerTitle.value = "My Cart"
        getCartViewModel().headerBottomLine.value = 1
        getCartViewModel().headerCartIcon.value = 0

        showBackButton?.let {
            getCartViewModel().headerBackButton.value = if (it) 1 else 0
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
                var product_id = cartItemsListWithoutRx[position]?.product.product_id
                var product_name = cartItemsListWithoutRx[position]?.product.title

                var productDetailsFragment = ProductDetailsFragment.newInstance(
                    product_id!!,
                    product_name
                )

                (activity as DashboardActivity).addOrReplaceFragment(productDetailsFragment, true)

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
                getCartViewModel().addToCart(productid!!, newQty)

            }

            override fun cartPlusClick(view: View, position: Int) {
                val product = cartItemsListWithoutRx[position]
                var productid = product.product.product_id
                var existingQty = product.qty
                var newQty = existingQty!! + 1
                getCartViewModel().addToCart(productid!!, newQty)
            }

            override fun cartDeleteClick(view: View, position: Int) {
                val product = cartItemsListWithoutRx[position]
                var productid = product.product.product_id
                getCartViewModel().addToCart(productid!!, 0)
            }
        })

        cartAdapterWithRx.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                var product_id = cartItemsListWithRx[position]?.product.product_id
                var product_name = cartItemsListWithRx[position]?.product.title

                var productDetailsFragment = ProductDetailsFragment.newInstance(
                    product_id!!,
                    product_name
                )

                (activity as DashboardActivity).addOrReplaceFragment(productDetailsFragment, true)
            }

            override fun cartMinusClick(view: View, position: Int) {
                val product = cartItemsListWithRx[position]
                var productid = product.product.product_id
                var existingQty = product.qty
                var newQty = existingQty!! - 1
                getCartViewModel().addToCart(productid!!, newQty)

            }

            override fun cartPlusClick(view: View, position: Int) {
                val product = cartItemsListWithRx[position]
                var productid = product.product.product_id
                var existingQty = product.qty
                var newQty = existingQty!! + 1
                getCartViewModel().addToCart(productid!!, newQty)
            }

            override fun cartDeleteClick(view: View, position: Int) {
                val product = cartItemsListWithRx[position]
                var productid = product.product.product_id
                getCartViewModel().addToCart(productid!!, 0)
            }
        })

        cartUploadedPrescriptionsAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                var fullscreenIntent =
                    Intent(requireActivity(), FullscreenImageViewerActivity::class.java)
                fullscreenIntent.putExtra("imageurl", uploadedPrescriptionsList[position].url)
                startActivity(fullscreenIntent)
            }

            override fun onDeletePrescriptionClick(view: View, position: Int) {
                var newarray = ArrayList<String>()
                for (i in uploadedPrescriptionsList.indices) {
                    if (i != position) {
                        newarray.add(uploadedPrescriptionsList[i].url)
                    }
                }

                getCartViewModel().addToCart(
                    0,
                    0,
                    TrollaConstants.ADDTOCART_TYPE_PRESCRIPTION,
                    newarray
                )
            }

        })

        binding.cartList.adapter = cartAdapterWithoutRx
        binding.cartListWithRequiredPrescription.adapter = cartAdapterWithRx
        binding.rvUploadedPrescriptions.adapter = cartUploadedPrescriptionsAdapter

        getCartViewModel().createOrderResponseLiveData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Resource.Success -> {


                    /* clear selected payment method and selected address from shared preferences after online order success*/

                    if (getCartViewModel().selectedPaymentModeLiveData.value == "COD") {
                        TrollaPreferencesManager.setString(
                            "",
                            TrollaPreferencesManager.PM_CART_SELECTED_PAYMENT_METHOD
                        )
                        TrollaPreferencesManager.setString(
                            "",
                            TrollaPreferencesManager.PM_CART_SELECTED_ADDRESS_ID
                        )
                        TrollaPreferencesManager.setString(
                            "",
                            TrollaPreferencesManager.PM_CART_SELECTED_ADDRESS_TITLE
                        )
                        TrollaPreferencesManager.setString(
                            "",
                            TrollaPreferencesManager.PM_CART_SELECTED_ADDRESS_ADDRESS
                        )
                    }
                    parentFragmentManager?.popBackStack()

                    (activity as DashboardActivity).addOrReplaceFragment(
                        OrderConfirmedFragment.newInstance(
                            it?.data?.data?.order?._id ?: "",
                            getCartViewModel().selectedPaymentModeLiveData.value
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

        Handler(Looper.getMainLooper()).postDelayed({ getCartViewModel().getCartDetails() }, 200)

        /*get selected payment method from shared preferences */
        if (!TrollaPreferencesManager.getString(TrollaPreferencesManager.PM_CART_SELECTED_PAYMENT_METHOD)
                .isNullOrEmpty()
        ) {
            getCartViewModel().selectedPaymentModeLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PM_CART_SELECTED_PAYMENT_METHOD)
            setSelectedPaymentMethod()
        }

        /* get selected address from shared preference */
        if (!TrollaPreferencesManager.getString(TrollaPreferencesManager.PM_CART_SELECTED_ADDRESS_ID)
                .isNullOrEmpty()
        ) {
            getCartViewModel().selectedAddressIdLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PM_CART_SELECTED_ADDRESS_ID)

            binding.rlSelectedDeliveryAddress.show()
            binding.llAddressNotAdded.hide()

            binding.txtAddressType.text =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PM_CART_SELECTED_ADDRESS_TITLE)

            binding.txtSelectedAddress.text =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PM_CART_SELECTED_ADDRESS_ADDRESS)
        }

        binding.rlCashonDelivery.setOnClickListener {
            getCartViewModel().selectedPaymentModeLiveData.value = "COD"
            setSelectedPaymentMethod()
            TrollaPreferencesManager.setString(
                "COD",
                TrollaPreferencesManager.PM_CART_SELECTED_PAYMENT_METHOD
            )
        }
        binding.rlPayOnline.setOnClickListener {
            getCartViewModel().selectedPaymentModeLiveData.value = "prepaid"
            setSelectedPaymentMethod()
            TrollaPreferencesManager.setString(
                "prepaid",
                TrollaPreferencesManager.PM_CART_SELECTED_PAYMENT_METHOD
            )
        }

        binding.llAddressNotAdded.setOnClickListener {
            var addressListFragment = AddressListFragment.newInstance("cart")
            (activity as DashboardActivity).addOrReplaceFragment(addressListFragment, true)
        }

        binding.txtChangeAddress.setOnClickListener {
            var addressListFragment = AddressListFragment.newInstance("cart")
            (activity as DashboardActivity).addOrReplaceFragment(addressListFragment, true)
        }

        getCartViewModel().progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        getCartViewModel().selectedAddressIdLiveData.observe(viewLifecycleOwner)
        {
            checkIfCartValid()
        }

        getCartViewModel().selectedPaymentModeLiveData.observe(viewLifecycleOwner)
        {
            checkIfCartValid()
        }

        binding.commonHeader.imgBack.setOnClickListener {
            parentFragmentManager?.popBackStack()
        }

        return binding.root
    }

    fun setSelectedPaymentMethod() {
        if (getCartViewModel().selectedPaymentModeLiveData.value == "COD") {
            binding.imgCOD.setImageResource(R.drawable.ic_cod)
            binding.imgOnline.setImageResource(R.drawable.ic_payonline_inactive)
            binding.imgCODSelected.setImageResource(R.drawable.ic_selected)
            binding.imgOnlineSelected.setImageResource(R.drawable.ic_unselected)
        } else if (getCartViewModel().selectedPaymentModeLiveData.value == "prepaid") {
            binding.imgCOD.setImageResource(R.drawable.ic_cod_inactive)
            binding.imgOnline.setImageResource(R.drawable.ic_payonline)
            binding.imgCODSelected.setImageResource(R.drawable.ic_unselected)
            binding.imgOnlineSelected.setImageResource(R.drawable.ic_selected)
        }
    }

    fun processCartData(
        cart: GetCartDetailsResponse.Cart
    ) {

        binding.txtCartTotal.text = getString(R.string.amount_string, cart.totalValue)
        binding.txtCartGST.text = getString(R.string.amount_string, cart.gst)
        binding.txtDeliveryFee.text = getString(R.string.amount_string, cart.deliveryFees)
        binding.txtDiscount.text = "-" + getString(R.string.amount_string, cart.totalDiscount)
        binding.txtTobePaid.text = getString(R.string.amount_string, cart.payable)
        binding.txtFinalPayable.text = getString(R.string.amount_string, cart.payable)

        if (cart.gst.toDouble() > 0) {
            binding.rlGST.show()
        } else {
            binding.rlGST.hide()
        }

        if (cart.totalDiscount.toDouble() > 0) {
            binding.rlDiscount.show()
            binding.txtSavings.show()

            binding.txtSavings.text = "You are saving " + getString(
                R.string.amount_string,
                cart.totalDiscount
            ) + " on this order"

        } else {
            binding.rlDiscount.hide()
            binding.txtSavings.hide()
        }

        cartItemsListWithoutRx.clear()
        cartItemsListWithRx.clear()
        uploadedPrescriptionsList.clear()

        for (i in cart.products!!.indices) {
            if (cart.products[i].product.rx_type == "NON-RX" || cart.products[i].product.rx_type == "") {
                cartItemsListWithoutRx.add(cart.products[i])
            } else {
                cartItemsListWithRx.add(cart.products[i])
            }
        }

        for (i in cart.prescriptions.indices) {
            uploadedPrescriptionsList.add(ModelPrescription(cart.prescriptions[i]))
        }

        cartAdapterWithoutRx.notifyDataSetChanged()
        cartAdapterWithRx.notifyDataSetChanged()
        cartUploadedPrescriptionsAdapter.notifyDataSetChanged()

        binding.llPrescriptionLayout.setVisibilityOnBoolean(cartItemsListWithRx.size != 0, true)

        binding.txtLabelPrescriptionNotRequired.setVisibilityOnBoolean(
            cartItemsListWithoutRx.size == 0,
            false
        )
        binding.cartList.setVisibilityOnBoolean(
            cartItemsListWithoutRx.size == 0,
            false
        )

        binding.txtLabelPrescriptionRequired.setVisibilityOnBoolean(
            cartItemsListWithRx.size == 0,
            false
        )
        binding.cartListWithRequiredPrescription.setVisibilityOnBoolean(
            cartItemsListWithRx.size == 0,
            false
        )

        binding.cartNesterScrollView.setVisibilityOnBoolean(cart.products.size == 0, false)
        binding.cardViewCartPayment.setVisibilityOnBoolean(cart.products.size == 0, false)
        binding.txtCartEmpty.setVisibilityOnBoolean(cart.products.size == 0, true)
        binding.rlSelectedDeliveryAddress.setVisibilityOnBoolean(
            getCartViewModel().selectedAddressIdLiveData.value == "",
            false
        )
        binding.llAddressNotAdded.setVisibilityOnBoolean(
            getCartViewModel().selectedAddressIdLiveData.value == "",
            true
        )

        if (!cart.prescriptions.isNullOrEmpty() && cartItemsListWithRx.size > 0) {
            binding.txtAddNewPrescription.show()
        } else {
            binding.txtAddNewPrescription.hide()
        }

        binding.rvUploadedPrescriptions.setVisibilityOnBoolean(
            cart.prescriptions == null || cart.prescriptions.size == 0,
            false
        )

        if (cart.prescriptions.isNullOrEmpty() && cartItemsListWithRx.size > 0) {
            binding.llUploadOptions.show()
        } else {
            binding.llUploadOptions.hide()
        }

        binding.txtAddNewPrescription.setOnClickListener {
            (activity as DashboardActivity).launchImagePicker("cart", ImageProvider.BOTH)
        }
        binding.llUploadUsingCamera.setOnClickListener {
            (activity as DashboardActivity).launchImagePicker("cart", ImageProvider.CAMERA)
        }
        binding.llUploadingUsingGallery.setOnClickListener {
            (activity as DashboardActivity).launchImagePicker("cart", ImageProvider.GALLERY)
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
    fun doThis(address: AddressSelectedEvent) {

        getCartViewModel().selectedAddressIdLiveData.value = address.modelAddress._id
        TrollaPreferencesManager.setString(
            address.modelAddress._id,
            TrollaPreferencesManager.PM_CART_SELECTED_ADDRESS_ID
        )

        binding.rlSelectedDeliveryAddress.setVisibilityOnBoolean(
            address.modelAddress == null,
            false
        )
        binding.llAddressNotAdded.setVisibilityOnBoolean(address.modelAddress == null, true)

        var addressTitle =
            address.modelAddress.name + " - " + address.modelAddress.contact //(address.modelAddress?.type ?: "Home").replaceFirstChar { it.uppercase() }
        binding.txtAddressType.text = addressTitle
        TrollaPreferencesManager.setString(
            addressTitle,
            TrollaPreferencesManager.PM_CART_SELECTED_ADDRESS_TITLE
        )

        var actualAddress =
            address.modelAddress.address + " " + address.modelAddress.landmark + " " + address.modelAddress.city + " " + address.modelAddress.state + "\n" + address.modelAddress.pincode
        binding.txtSelectedAddress.text = actualAddress

        TrollaPreferencesManager.setString(
            actualAddress,
            TrollaPreferencesManager.PM_CART_SELECTED_ADDRESS_ADDRESS
        )
    }

    fun checkIfCartValid() {

        if (getCartViewModel().selectedPaymentModeLiveData.value == "" || getCartViewModel().selectedAddressIdLiveData.value == "") {
            getCartViewModel().isCartValid.value = false
        } else getCartViewModel().isCartValid.value =
            !(cartItemsListWithRx.size != 0 && uploadedPrescriptionsList.size == 0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(prescriptionUploadedEvent: PrescriptionUploadedEvent) {
        var newarray = ArrayList<String>()
        for (i in uploadedPrescriptionsList.indices) {
            newarray.add(uploadedPrescriptionsList[i].url)
        }
        newarray.add(prescriptionUploadedEvent.prescription_url)
        getCartViewModel().addToCart(0, 0, TrollaConstants.ADDTOCART_TYPE_PRESCRIPTION, newarray)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(cartDetailsRefreshedEvent: CartDetailsRefreshedEvent) {
        getCartViewModel().cartDetailsResponseLiveData.value?.data?.data?.cart?.let { cart ->
            processCartData(cart)
        }
        getCartViewModel().cartDetailsResponseLiveData.value?.let {
            it?.data?.data?.payment_options?.let { paymentOptions ->
                var isCODAvailable = false
                var codAvailableTitle = "Cash On Delivery"
                var isOnlineAvailable = false
                var onlineAvailableTitle = "Online Payment"
                for (i in paymentOptions.indices) {
                    if (paymentOptions[i].payment_mode == "COD") {
                        if (!isCODAvailable) {
                            isCODAvailable = true
                            codAvailableTitle = "Cash On Delivery"//paymentOptions[i].name
                        }
                    } else if (paymentOptions[i].payment_mode == "prepaid") {
                        if (!isOnlineAvailable) {
                            isOnlineAvailable = true
                            onlineAvailableTitle = "Online Payment" //paymentOptions[i].name
                        }
                    }
                }

                binding.rlCashonDelivery.setVisibilityOnBoolean(isCODAvailable, true)
                binding.rlPayOnline.setVisibilityOnBoolean(isOnlineAvailable, true)
                binding.txtCODTitle.text = codAvailableTitle
                binding.txtOnline.text = onlineAvailableTitle
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(addToCartSuccessEvent: AddToCartSuccessEvent) {
        getCartViewModel().addToCartResponseLiveData.value?.data?.data?.cart?.let { cart ->
            processCartData(cart)
        }

        (activity as DashboardActivity).showPrescriptionUploadedSuccessDialogue()
        checkIfCartValid()

    }

}