package com.trolla.healthsdk.feature_orders.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.GenericAdapter
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.FragmentOrdersListBinding
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_orders.data.EventRefreshOrders
import com.trolla.healthsdk.feature_orders.data.ModelOrder
import com.trolla.healthsdk.ui_utils.WebviewActivity
import com.trolla.healthsdk.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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
                    ordersList[position].order_id,
                    ordersList[position].wf_order_id ?: ""
                )
                (activity as DashboardActivity).addOrReplaceFragment(ordersDetailsFragment, true)
            }

            override fun onTrackOrderClick(view: View, position: Int) {
                initiateTracking(ordersList[position].order_id, ordersList[position].tracking_url)
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
                    if (ordersList.size > 0) {
                        binding.llNoRecords.hide()
                    } else {
                        binding.llNoRecords.show()
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

        orderListViewModel.progressStatus.observe(viewLifecycleOwner) {
            (activity as DashboardActivity).showHideProgressBar(it)
        }

        binding.commonHeader.imgBack.setOnClickListener {
            parentFragmentManager?.popBackStack()
        }

        //orderListViewModel.getOrdersList()
        orderListViewModel.getProfile()

        return binding.root
    }

    private fun initiateTracking(orderid: String, trackingUrl: String) {
        var intent = Intent(requireActivity(), WebviewActivity::class.java)
        intent.putExtra("title", "Order ID: $orderid")
        intent.putExtra("url", trackingUrl)
        startActivity(intent)
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
    fun doThis(eventRefreshOrders: EventRefreshOrders) {
        LogUtil.printObject("-----> eventRefreshOrders event called")
        Handler(Looper.getMainLooper()).postDelayed({ orderListViewModel.getOrdersList() }, 200)

    }
}