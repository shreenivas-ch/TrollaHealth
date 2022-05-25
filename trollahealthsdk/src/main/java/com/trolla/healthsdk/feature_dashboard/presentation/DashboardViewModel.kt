package com.trolla.healthsdk.feature_dashboard.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.domain.usecases.GetDashboardUsecase
import kotlinx.coroutines.launch

class DashboardViewModel(private val getDashboardUsecase: GetDashboardUsecase) : ViewModel() {

    val dashboardResponseLiveData = MutableLiveData<Resource<BaseApiResponse<DashboardResponse>>>()

    fun getDashboard() {
        viewModelScope.launch {
            dashboardResponseLiveData.value = getDashboardUsecase()!!
        }
    }
}