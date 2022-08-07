package com.trolla.healthsdk.feature_orders.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.CustomBindingAdapter
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.CartFragmentBinding
import com.trolla.healthsdk.databinding.FragmentOrdersDetailsBinding
import com.trolla.healthsdk.databinding.FragmentOrdersListBinding
import com.trolla.healthsdk.feature_address.data.AddressSelectedEvent
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_address.presentation.AddressListFragment
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_orders.data.ModelOrder
import com.trolla.healthsdk.feature_payment.presentation.PaymentGatewayIntegrationFragment
import com.trolla.healthsdk.feature_prescriptionupload.data.ModelPrescription
import com.trolla.healthsdk.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class OrdersDetailsFragment : Fragment() {

    companion object {
        fun newInstance(orderid: String, ordernumber: String): OrdersDetailsFragment {
            var bundle = Bundle()
            bundle.putString("orderid", orderid)
            bundle.putString("ordernumber", ordernumber)
            var ordersDetailsFragment = OrdersDetailsFragment()
            ordersDetailsFragment.arguments = bundle
            return ordersDetailsFragment
        }
    }

    val orderDetailsViewModel: OrderDetailsViewModel by inject(OrderDetailsViewModel::class.java)
    lateinit var binding: FragmentOrdersDetailsBinding

    val orderid by lazy {
        arguments?.let {
            it.getString("orderid")
        }
    }
    val ordernumber by lazy {
        arguments?.let {
            it.getString("ordernumber")
        }
    }

    var transactionid: String = ""
    var amount: String = ""
    var rarorpay_orderid: String = ""

    var cartItemsListWithoutRx = ArrayList<GetCartDetailsResponse.CartProduct>()
    var cartItemsListWithRx = ArrayList<GetCartDetailsResponse.CartProduct>()
    var uploadedPrescriptionsList = ArrayList<ModelPrescription>()
    lateinit var cartAdapterWithoutRx: GenericAdapter<GetCartDetailsResponse.CartProduct>
    lateinit var cartAdapterWithRx: GenericAdapter<GetCartDetailsResponse.CartProduct>
    lateinit var cartUploadedPrescriptionsAdapter: GenericAdapter<ModelPrescription>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_orders_details,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = orderDetailsViewModel

        orderDetailsViewModel.headerTitle.value = "Order ID: $ordernumber"
        orderDetailsViewModel.headerBottomLine.value = 1
        orderDetailsViewModel.headerBackButton.value = 1

        binding.commonHeader.imgBack.setOnClickListener {
            parentFragmentManager?.popBackStack()
        }

        cartAdapterWithoutRx = GenericAdapter(
            R.layout.item_order_product,
            cartItemsListWithoutRx
        )

        cartAdapterWithRx = GenericAdapter(
            R.layout.item_order_product,
            cartItemsListWithRx
        )

        cartUploadedPrescriptionsAdapter = GenericAdapter(
            R.layout.item_uploaded_prescription,
            uploadedPrescriptionsList
        )

        binding.cartList.adapter = cartAdapterWithoutRx
        binding.cartListWithRequiredPrescription.adapter = cartAdapterWithRx
        binding.rvUploadedPrescriptions.adapter = cartUploadedPrescriptionsAdapter

        orderDetailsViewModel.orderDetailsResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {

                    if (it?.data?.data?.order?.transactions!![0].status.lowercase() == "pending") {
                        if (it?.data?.data?.order?.transactions!![0].mode.lowercase() == "prepaid") {
                            binding.txtPay.text = "Pay " + getString(
                                R.string.amount,
                                it?.data?.data?.order?.transactions!![0].amount
                            )

                            transactionid = it?.data?.data?.order?.transactions!![0]._id
                            amount = it?.data?.data?.order?.amount
                            rarorpay_orderid = it?.data?.data?.order?.rarorpay_orderid
                        } else {
                            binding.txtPay.hide()
                        }
                    }

                    cartItemsListWithoutRx.clear()
                    cartItemsListWithRx.clear()
                    uploadedPrescriptionsList.clear()

                    var order = it?.data?.data?.order
                    for (i in order?.products!!.indices) {
                        if (order?.products[i].product.rx_type == "NON-RX" || order?.products[i].product.rx_type == "NON-RX") {
                            cartItemsListWithoutRx.add(order.products[i])
                        } else {
                            cartItemsListWithRx.add(order.products[i])
                        }
                        LogUtil.printObject("----->: " + order.products[i].qty)
                    }

                    for (i in order?.prescriptions.indices) {
                        uploadedPrescriptionsList.add(ModelPrescription(order.prescriptions[i]))
                    }

                    cartAdapterWithoutRx.notifyDataSetChanged()
                    cartAdapterWithRx.notifyDataSetChanged()
                    cartUploadedPrescriptionsAdapter.notifyDataSetChanged()

                    binding.rvUploadedPrescriptions.setVisibilityOnBoolean(
                        uploadedPrescriptionsList.size == 0,
                        false
                    )
                    binding.cartList.setVisibilityOnBoolean(cartItemsListWithoutRx.size == 0, false)
                    binding.cartListWithRequiredPrescription.setVisibilityOnBoolean(
                        cartItemsListWithRx.size == 0,
                        false
                    )

                    binding.txtLabelPrescriptionNotRequired.setVisibilityOnBoolean(
                        cartItemsListWithoutRx.size == 0,
                        false
                    )
                    binding.txtLabelPrescriptionRequired.setVisibilityOnBoolean(
                        cartItemsListWithRx.size == 0,
                        false
                    )
                    binding.txtUploadedPrescriptions.setVisibilityOnBoolean(
                        uploadedPrescriptionsList.size == 0,
                        false
                    )

                    CustomBindingAdapter.setOrderDateTime(
                        binding.txtOrderDateTime,
                        order.status,
                        order.created_at
                    )

                    binding.txtOrderStatus.text = order.status.replaceFirstChar(Char::uppercase)

                    CustomBindingAdapter.setOrderStatusIcon(binding.imgOrderStatus, order.status)

                    binding.txtTotalPrice.text = getString(R.string.amount_string, amount)
                    binding.txtPaymentMode.text =
                        it?.data?.data?.order?.transactions!![0].mode.replaceFirstChar(Char::uppercase)

                    binding.txtAddressType.text =
                        if (order.address.type.isNullOrEmpty()) "Home" else order.address.type
                    binding.txtSelectedAddress.text =
                        order.address.name + "\n" + order.address.address + " " + order.address.landmark + " " + order.address.city + " " + order.address.state + "\n" + order.address.pincode

                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        binding.txtPay.setOnClickListener {
            /*(activity as DashboardActivity).addOrReplaceFragment(
                PaymentGatewayIntegrationFragment.newInstance(transactionid, amount,rarorpay_orderid),
                true
            )*/

            (activity as DashboardActivity).startRazorPay(
                amount,
                transactionid,
                rarorpay_orderid
            )
        }

        orderDetailsViewModel.getOrderDetails(orderid!!)

        return binding.root
    }

}