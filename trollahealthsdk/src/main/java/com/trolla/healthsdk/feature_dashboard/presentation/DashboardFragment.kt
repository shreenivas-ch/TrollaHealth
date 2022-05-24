package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.ComponentGenerator
import com.trolla.healthsdk.databinding.FragmentDashboardBinding
import com.trolla.healthsdk.feature_dashboard.data.DashboardComponentModel

class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_dashboard,
            container,
            false
        )

        attachFragmentsToView()

        return binding.root
    }

    fun attachFragmentsToView() {

        var dashboardComponentModel = DashboardComponentModel<String>("dashboardBanner", "dummy")
        var fragment = ComponentGenerator.getComponentObject(dashboardComponentModel)

        if (fragment != null) {
            childFragmentManager?.beginTransaction()
                .add(binding.llViewContainer?.id!!, fragment)
                .commit()
        }

    }
}