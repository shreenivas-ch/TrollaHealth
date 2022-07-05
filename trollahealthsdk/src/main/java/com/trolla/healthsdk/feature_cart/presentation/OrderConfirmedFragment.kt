package com.trolla.healthsdk.feature_cart.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.databinding.FragmentOrderConfirmedBinding
import com.trolla.healthsdk.feature_cart.data.models.RefreshCartEvent
import com.trolla.healthsdk.feature_dashboard.data.RefreshDashboardEvent
import com.trolla.healthsdk.feature_dashboard.presentation.DashboardActivity
import com.trolla.healthsdk.feature_orders.presentation.OrdersListFragment
import org.greenrobot.eventbus.EventBus

class OrderConfirmedFragment : Fragment() {

    companion object {
        fun newInstance(orderId: String): OrderConfirmedFragment {
            var bundle = Bundle()
            bundle.putString("orderId", orderId)
            var orderConfirmedFragment = OrderConfirmedFragment()
            orderConfirmedFragment.arguments = bundle
            return orderConfirmedFragment
        }
    }

    val orderId by lazy {
        arguments?.let {
            it.getString("orderId")
        }
    }

    lateinit var binding: FragmentOrderConfirmedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_confirmed,
            container,
            false
        )

        binding.lifecycleOwner = this

        binding.txtOrderDetails.text = "Your Order $orderId has been placed Successfully."
        binding.txtGotoHomePage.setOnClickListener {
            parentFragmentManager?.popBackStack()
            (activity as DashboardActivity).addOrReplaceFragment(
                OrdersListFragment.newInstance(),
                true
            )
        }

        binding.txtTrackOrder.setOnClickListener {
            parentFragmentManager?.popBackStack()
            (activity as DashboardActivity).addOrReplaceFragment(
                OrdersListFragment.newInstance(),
                true
            )
        }

        EventBus.getDefault().post(RefreshDashboardEvent())
        EventBus.getDefault().post(RefreshCartEvent())

        return binding.root
    }


}