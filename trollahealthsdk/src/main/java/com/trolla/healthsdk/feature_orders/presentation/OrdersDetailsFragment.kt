package com.trolla.healthsdk.feature_orders.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.freshchat.consumer.sdk.Freshchat
import com.freshchat.consumer.sdk.FreshchatConfig
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.CustomBindingAdapter
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.databinding.FragmentOrdersDetailsBinding
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.presentation.CartFragment
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_orders.data.OrderDetailsResponse
import com.trolla.healthsdk.feature_prescriptionupload.data.ModelPrescription
import com.trolla.healthsdk.ui_utils.WebviewActivity
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
    var trackingUrl: String = ""

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

        orderDetailsViewModel.cancelOrderResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    orderDetailsViewModel.getOrderDetails(orderid!!)
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        orderDetailsViewModel.repeatOrderResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    parentFragmentManager.popBackStack()
                    var cartFragment = CartFragment.newInstance()
                    (activity as DashboardActivity).addOrReplaceFragment(cartFragment, true)
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        orderDetailsViewModel.orderDetailsResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {

                    handleButtonsVisibility(it)

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

                    if (order.order_value.gst.toDouble() > 0) {
                        binding.rlGST.show()
                    } else {
                        binding.rlGST.hide()
                    }

                    if (order.order_value.totalDiscount.toDouble() > 0) {
                        binding.rlDiscount.show()
                    } else {
                        binding.rlDiscount.hide()
                    }

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

                    if (!it?.data?.data?.order?.tracking_url.isNullOrEmpty()) {
                        trackingUrl = it?.data?.data?.order?.tracking_url
                    }

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

        binding.txtTrackOrder.setOnClickListener {
            initiateTracking(orderid.toString(), trackingUrl)
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
        orderDetailsViewModel.getProfile()

        binding.txtChatWithUs.setOnClickListener {
            initiateChatSupport()
        }

        binding.txtCancelOrder.setOnClickListener {
            orderDetailsViewModel.cancelOrder(orderid!!)
        }

        binding.txtRepeatOrder.setOnClickListener {
            orderDetailsViewModel.repeatOrder(orderid!!)
        }

        return binding.root
    }

    private fun initiateTracking(orderid: String, trackingUrl: String) {
        var intent = Intent(requireActivity(), WebviewActivity::class.java)
        intent.putExtra("title", "Order ID: $orderid")
        intent.putExtra("url", trackingUrl)
        startActivity(intent)
    }

    private fun initiateChatSupport() {
        val freshchatConfig = FreshchatConfig(
            "2013a117-4341-45f5-b68c-7b8948eb40d9",
            "b4af71ce-0fa1-4154-8d40-d76fc49909de"
        )
        freshchatConfig.domain = "msdk.in.freshchat.com"
        Freshchat.getInstance(activity?.applicationContext!!).init(freshchatConfig)

        val freshchatUser =
            Freshchat.getInstance(activity?.applicationContext!!).user
        freshchatUser.firstName = orderDetailsViewModel.profileNameLiveData?.value ?: "Guest"
        freshchatUser.email = orderDetailsViewModel.profileEmailLiveData?.value ?: "guest@guest.com"
        freshchatUser.setPhone(
            "+91",
            orderDetailsViewModel.profileMobileLiveData?.value ?: "9000000001"
        )

        Freshchat.getInstance(activity?.applicationContext!!).user = freshchatUser

        Freshchat.showConversations(activity?.applicationContext!!)
    }

    private fun handleButtonsVisibility(response: Resource.Success<BaseApiResponse<OrderDetailsResponse>>) {
        var orderStatus = response.data?.data?.order?.status?.lowercase()
        var tracking_url = response.data?.data?.order?.tracking_url

        /*Pay Button*/

        if (orderStatus!!.contains(TrollaConstants.ORDERSTATUS_DELIVERED) || orderStatus.contains(
                TrollaConstants.ORDERSTATUS_CANCEL
            )
        ) {
            binding.txtPay.hide()
        } else {
            var transactionStatus = ""
            if (response.data?.data?.order?.transactions != null && response.data?.data?.order?.transactions?.size > 0) {
                transactionStatus = response.data?.data?.order?.transactions!![0].status.lowercase()
            }

            if (transactionStatus.contains(TrollaConstants.ORDERSTATUS_PENDING) || transactionStatus.contains(
                    TrollaConstants.ORDERSTATUS_FAILURE
                )
            ) {
                binding.txtPay.show()
                binding.txtPay.text = "Pay " + getString(
                    R.string.amount,
                    response.data?.data?.order?.transactions!![0].amount
                ) + " Online Now"
            } else {
                binding.txtPay.hide()
            }
        }

        /*Track Order button*/
        if (tracking_url.isNullOrEmpty()) {
            binding.txtTrackOrder.hide()
        } else {
            if (orderStatus.contains(TrollaConstants.ORDERSTATUS_DELIVERED) || orderStatus.contains(
                    TrollaConstants.ORDERSTATUS_CANCEL
                )
            ) {
                binding.txtTrackOrder.hide()
            } else {
                binding.txtTrackOrder.show()
            }
        }

        /*chat with us - No Conditions */

        /*Download Invoice*/
        if (orderStatus.contains(TrollaConstants.ORDERSTATUS_DELIVERED)) {
            binding.txtDownloadInvoice.show()
        } else {
            binding.txtDownloadInvoice.hide()
        }

        /*Cancel Order Button */
        if (orderStatus.contains(TrollaConstants.ORDERSTATUS_DELIVERED) || orderStatus.contains(
                TrollaConstants.ORDERSTATUS_CANCEL
            )
        ) {
            binding.txtCancelOrder.hide()
        } else {
            binding.txtCancelOrder.show()
        }

        /*Cancel Order Button */
        if (orderStatus.contains(TrollaConstants.ORDERSTATUS_DELIVERED)) {
            binding.txtRepeatOrder.show()
        } else {
            binding.txtRepeatOrder.hide()
        }

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