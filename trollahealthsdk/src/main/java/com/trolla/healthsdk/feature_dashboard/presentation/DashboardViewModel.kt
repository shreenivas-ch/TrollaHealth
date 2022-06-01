package com.trolla.healthsdk.feature_dashboard.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.domain.usecases.GetDashboardUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import kotlinx.coroutines.launch

class DashboardViewModel(private val getDashboardUsecase: GetDashboardUsecase) : BaseViewModel() {

    val dashboardResponseLiveData = MutableLiveData<Resource<BaseApiResponse<DashboardResponse>>>()

    fun getDashboard() {
        progressStatus.value = true
        viewModelScope.launch {
            progressStatus.value = false
            dashboardResponseLiveData.value = getDashboardUsecase()!!
        }
    }
}