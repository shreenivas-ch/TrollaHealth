package com.trolla.healthsdk.feature_orders.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.freshchat.consumer.sdk.Freshchat
import com.freshchat.consumer.sdk.FreshchatConfig
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.CartFragmentBinding
import com.trolla.healthsdk.databinding.FragmentOrdersListBinding
import com.trolla.healthsdk.feature_address.data.AddressSelectedEvent
import com.trolla.healthsdk.feature_address.data.ModelAddress
import com.trolla.healthsdk.feature_address.presentation.AddressListFragment
import com.trolla.healthsdk.feature_cart.data.GetCartDetailsResponse
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_orders.data.ModelOrder
import com.trolla.healthsdk.feature_prescriptionupload.data.ModelPrescription
import com.trolla.healthsdk.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class OrdersListFragment : Fragment() {

    companion object {
        fun newInstance(): OrdersListFragment {
            var cartFragment = OrdersListFragment()
            return cartFragment
        }
    }

    val orderListViewModel: OrderListViewModel by inject(OrderListViewModel::class.java)
    lateinit var binding: FragmentOrdersListBinding

    var ordersList = ArrayList<ModelOrder>()
    lateinit var orderAdapter: GenericAdapter<ModelOrder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_orders_list,
            container,
            false
        )

        binding.lifecycleOwner = this
        binding.viewModel = orderListViewModel

        orderListViewModel.headerTitle.value = "My Orders"
        orderListViewModel.headerBottomLine.value = 1
        orderListViewModel.headerBackButton.value = 1

        orderAdapter = GenericAdapter(
            R.layout.item_orderslist_order,
            ordersList
        )

        orderAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int) {
                var ordersDetailsFragment = OrdersDetailsFragment.newInstance(
                    ordersList[position]._id,
                    ordersList[position].order_id
                )
                (activity as DashboardActivity).addOrReplaceFragment(ordersDetailsFragment, true)
            }

            override fun onTrackOrderClick(view: View, position: Int) {
                initiateChatSupport()
            }
        })

        binding.rvOrdersList.adapter = orderAdapter

        orderListViewModel.orderListReposponseLiveData.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is Resource.Success -> {
                    ordersList.clear()
                    ordersList.addAll(it?.data?.data?.orders!!)
                    orderAdapter.notifyDataSetChanged()
                }

                is Resource.Error -> {
                    TrollaHealthUtility.showAlertDialogue(
                        requireContext(),
                        it.uiText?.asString(requireContext())
                    )
                }
            }
        }

        orderListViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        binding.commonHeader.imgBack.setOnClickListener {
            parentFragmentManager?.popBackStack()
        }


        orderListViewModel.getOrdersList()
        orderListViewModel.getProfile()

        return binding.root
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
        freshchatUser.firstName = orderListViewModel.profileNameLiveData?.value ?: "Guest"
        freshchatUser.email = orderListViewModel.profileEmailLiveData?.value ?: "guest@guest.com"
        freshchatUser.setPhone(
            "+91",
            orderListViewModel.profileMobileLiveData?.value ?: "9000000001"
        )

        Freshchat.getInstance(activity?.applicationContext!!).user = freshchatUser

        Freshchat.showConversations(activity?.applicationContext!!)
    }

}