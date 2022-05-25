package com.trolla.healthsdk.feature_dashboard.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.R
import com.trolla.healthsdk.core.ComponentGenerator
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.databinding.FragmentDashboardBinding
import com.trolla.healthsdk.feature_auth.presentation.AuthenticationActivity
import com.trolla.healthsdk.feature_auth.presentation.LoginEmailViewModel
import com.trolla.healthsdk.feature_auth.presentation.LoginOTPVerificationFragment
import com.trolla.healthsdk.feature_dashboard.data.DashboardComponentModel
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse.HomePagePositionsListItem.BannerData
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.asString
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding

    val dashboardViewModel: DashboardViewModel by inject(DashboardViewModel::class.java)

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

        binding.lifecycleOwner = this

        dashboardViewModel.getDashboard()

        dashboardViewModel.dashboardResponseLiveData.observe(viewLifecycleOwner)
        {
            when (it) {
                is Resource.Success -> {

                    var response = dashboardViewModel.dashboardResponseLiveData.value
                    response?.data?.data?.homePagePositionsList?.forEach { homepageitem ->
                        if (homepageitem.name == "Placeholder 2") {
                            var dashboardComponentModel =
                                DashboardComponentModel("dashboardBanner", homepageitem.banner_data)
                            var fragment =
                                ComponentGenerator.getComponentObject(dashboardComponentModel)

                            if (fragment != null) {
                                childFragmentManager?.beginTransaction()
                                    .add(binding.llViewContainer?.id!!, fragment)
                                    .commit()
                            }
                        }
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

        return binding.root
    }
}