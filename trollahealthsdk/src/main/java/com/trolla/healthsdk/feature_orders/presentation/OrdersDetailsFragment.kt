package com.trolla.healthsdk.feature_orders.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.CustomBindingAdapter
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.databinding.FragmentOrdersDetailsBinding
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_cart.presentation.CartFragment
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_dashboard.presentation.initiateChatSupport
import com.trolla.healthsdk.feature_orders.data.OrderDetailsResponse
import com.trolla.healthsdk.feature_prescriptionupload.data.ModelPrescription
import com.trolla.healthsdk.feature_productdetails.presentation.FullscreenImageViewerActivity
import com.trolla.healthsdk.feature_productdetails.presentation.ProductDetailsFragment
import com.trolla.healthsdk.ui_utils.WebviewActivity
import com.trolla.healthsdk.utils.*
import org.koin.java.KoinJavaComponent.inject

class OrdersDetailsFragment : Fragment() {

    companion object {
        fun newInstance(
            orderid: String,
            ordernumber: String,
            wf_order_id: String
        ): OrdersDetailsFragment {
            var bundle = Bundle()
            bundle.putString("orderid", orderid)
            bundle.putString("ordernumber", ordernumber)
            bundle.putString("wf_order_id", wf_order_id)
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
    val wf_order_id by lazy {
        arguments?.let {
            it.getString("wf_order_id")
        }
    }

    var transactionid: String = ""
    var amount: String = ""
    var rarorpay_orderid: String = ""
    var payable_amount: String = ""
    var trackingUrl: String = ""
    var invoiceUrl: String = ""

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

        cartUploadedPrescriptionsAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                var fullscreenIntent =
                    Intent(requireActivity(), FullscreenImageViewerActivity::class.java)
                fullscreenIntent.putExtra("imageurl", uploadedPrescriptionsList[position].url)
                startActivity(fullscreenIntent)
            }

        })

        binding.cartList.adapter = cartItemsAdapter
        binding.rvUploadedPrescriptions.adapter = cartUploadedPrescriptionsAdapter

        cartItemsAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                var product_id = cartItems[position]?.product.product_id
                var product_name = cartItems[position]?.product.title

                var productDetailsFragment = ProductDetailsFragment.newInstance(
                    product_id!!,
                    product_name
                )

                (activity as DashboardActivity).addOrReplaceFragment(productDetailsFragment, true)
            }

        })

        orderDetailsViewModel.cancelOrderResponseLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    orderDetailsViewModel.getOrderDetails(orderid!!, wf_order_id ?: "")
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

                    binding.llMainView.show()
                    binding.orderActions.show()

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
                        order.eta
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
                        binding.txtSavings.show()

                        binding.txtSavings.text = "You saved " + getString(
                            R.string.amount_string,
                            order.order_value.totalDiscount
                        ) + " on this order"
                    } else {
                        binding.rlDiscount.hide()
                        binding.txtSavings.hide()
                    }

                    if (it.data.data.order.transactions != null && it.data.data.order.transactions.size > 0) {
                        binding.txtPaymentMode.text =
                            it.data.data.order.transactions[0].mode.replaceFirstChar(Char::uppercase)
                    } else {
                        binding.txtPaymentMode.text = "Cash On Delivery"
                    }

                    binding.txtAddressType.text = order.address.name + " - " + order.address.contact
                    binding.txtSelectedAddress.text =
                        order.address.address + " " + order.address.landmark + " " + order.address.city + " " + order.address.state + "\n" + order.address.pincode

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

        binding.txtDownloadInvoice.setOnClickListener {
            downloadInvoice(invoiceUrl)
        }

        binding.txtChatSupport.setOnClickListener {
            initiateChatSupport()
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

        Handler(Looper.getMainLooper()).postDelayed({
            orderDetailsViewModel.getOrderDetails(
                orderid!!,
                wf_order_id ?: ""
            )
        }, 200)

        orderDetailsViewModel.getProfile()

        binding.txtChatSupport.setOnClickListener {
            initiateChatSupport()
        }

        binding.txtCancelOrder.setOnClickListener {
            orderDetailsViewModel.cancelOrder(orderid!!, wf_order_id ?: "")
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

    private fun downloadInvoice(invoiceurl: String) {
        val urlIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(invoiceurl)
        )
        startActivity(urlIntent)
    }

    private fun handleButtonsVisibility(response: Resource.Success<BaseApiResponse<OrderDetailsResponse>>) {
        var orderStatus = response.data?.data?.order?.status?.lowercase()
        trackingUrl = response.data?.data?.order?.tracking_url ?: ""
        invoiceUrl = response.data?.data?.order?.invoice_url ?: ""
        var eta = response.data?.data?.order?.eta ?: ""

        /*Pay Button*/

        if (orderStatus!!.contains(TrollaConstants.ORDERSTATUS_DELIVERED) || orderStatus.contains(
                TrollaConstants.ORDERSTATUS_CANCEL
            )
        ) {
            binding.txtPay.hide()
        } else {
            var transactionStatus = ""
            if (response.data?.data?.order?.transactions != null && response.data?.data?.order?.transactions?.size > 0) {
                transactionStatus =
                    response.data?.data?.order?.transactions!![response.data?.data?.order?.transactions?.size - 1].status.lowercase()
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
        if (trackingUrl.isNullOrEmpty()) {
            binding.txtTrackOrder.hide()
        } else {
            if (orderStatus.contains(TrollaConstants.ORDERSTATUS_IN_TRANSIT)
            ) {
                binding.txtTrackOrder.show()
            } else {
                binding.txtTrackOrder.hide()
            }
        }

        /*chat with us - No Conditions */

        /*Download Invoice*/
        if (orderStatus.contains(TrollaConstants.ORDERSTATUS_DELIVERED) && !invoiceUrl.isNullOrEmpty()) {
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
        if (orderStatus.contains(TrollaConstants.ORDERSTATUS_DELIVERED) || orderStatus.contains(
                TrollaConstants.ORDERSTATUS_CANCEL
            )
        ) {
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