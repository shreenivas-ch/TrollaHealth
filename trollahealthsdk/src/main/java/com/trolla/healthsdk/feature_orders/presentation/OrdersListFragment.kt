package com.trolla.healthsdk.feature_orders.presentation

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

        orderListViewModel.getOrdersList()

        return binding.root
    }

}