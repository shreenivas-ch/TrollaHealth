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
import com.trolla.healthsdk.databinding.FragmentOrdersDetailsBinding
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_prescriptionupload.data.ModelPrescription
import com.trolla.healthsdk.utils.*
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
    var payable_amount: String = ""

    var cartItems = ArrayList<GetCartDetailsResponse.CartProduct>()
    var uploadedPrescriptionsList = ArrayList<ModelPrescription>()
    lateinit var cartItemsAdapter: GenericAdapter<GetCartDetailsResponse.CartProduct>
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
            parentFragmentManager.popBackStack()
        }

        cartItemsAdapter = GenericAdapter(
            R.layout.item_order_product,
            cartItems
        )

        cartUploadedPrescriptionsAdapter = GenericAdapter(
            R.layout.item_uploaded_prescription,
            uploadedPrescriptionsList
        )

        binding.cartList.adapter = cartItemsAdapter
        binding.rvUploadedPrescriptions.adapter = cartUploadedPrescriptionsAdapter

        orderDetailsViewModel.orderDetailsResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (it.data?.data?.order?.transactions != null && it.data?.data?.order?.transactions?.size > 0) {
                        if (it.data?.data?.order?.transactions!![0].status.lowercase() == "pending" || it.data?.data?.order?.transactions!![0].status.lowercase() == "failure") {
                            binding.txtPay.show()
                            binding.txtPay.text = "Pay " + getString(
                                R.string.amount,
                                it.data.data.order.transactions[0].amount
                            ) + " Online Now"
                        } else {
                            binding.txtPay.hide()
                        }
                    } else {
                        binding.txtPay.show()
                        binding.txtPay.text = "Pay " + getString(
                            R.string.amount,
                            it.data?.data?.order?.order_value?.payable
                        ) + " Online Now"
                    }

                    amount = it.data?.data?.order?.order_value?.payable!!
                    cartItems.clear()
                    uploadedPrescriptionsList.clear()

                    var order = it.data.data.order
                    for (i in order.products.indices) {
                        cartItems.add(order.products[i])
                        LogUtil.printObject("----->: " + order.products[i].qty)
                    }

                    for (i in order.prescriptions.indices) {
                        uploadedPrescriptionsList.add(
                            ModelPrescription(
                                order.prescriptions[i],
                                false
                            )
                        )
                    }

                    cartItemsAdapter.notifyDataSetChanged()
                    cartUploadedPrescriptionsAdapter.notifyDataSetChanged()

                    binding.rvUploadedPrescriptions.setVisibilityOnBoolean(
                        uploadedPrescriptionsList.size == 0,
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

                    binding.txtCartTotal.text =
                        getString(R.string.amount_string, order.order_value.totalValue)
                    binding.txtCartGST.text =
                        getString(R.string.amount_string, order.order_value.gst)
                    binding.txtDeliveryFee.text =
                        getString(R.string.amount_string, order.order_value.deliveryFees)
                    binding.txtDiscount.text =
                        "-" + getString(R.string.amount_string, order.order_value.totalDiscount)
                    binding.txtTobePaid.text =
                        getString(R.string.amount_string, order.order_value.payable)

                    if (it.data.data.order.transactions != null && it.data.data.order.transactions.size > 0) {
                        binding.txtPaymentMode.text =
                            it.data.data.order.transactions[0].mode.replaceFirstChar(Char::uppercase)
                    } else {
                        binding.txtPaymentMode.text = "Cash On Delivery"
                    }

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
            orderDetailsViewModel.getTransactionID(orderid.toString(), "prepaid")
        }

        orderDetailsViewModel.getTransactionIDLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {
                    it.data?.data?.transaction?.let { transaction ->
                        rarorpay_orderid = transaction.payment_gateway_ref.id
                        transactionid = transaction._id
                        payable_amount = transaction.amount
                    }
                    callPaymentGateway()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )

                    parentFragmentManager.popBackStack()
                }
            }
        }

        orderDetailsViewModel.getOrderDetails(orderid!!)

        return binding.root
    }

    fun callPaymentGateway() {
        (activity as DashboardActivity).startRazorPay(
            amount,
            payable_amount,
            rarorpay_orderid
        )

        (activity as DashboardActivity).transaction_id = transactionid
        (activity as DashboardActivity).paymentRedirectionScreen = "orderdetails"
        parentFragmentManager.popBackStack()
    }

}