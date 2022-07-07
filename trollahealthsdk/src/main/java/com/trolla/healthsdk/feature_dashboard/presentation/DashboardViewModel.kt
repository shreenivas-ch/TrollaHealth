package com.trolla.healthsdk.feature_dashboard.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_auth.data.models.UpdateProfileResponse
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_dashboard.domain.usecases.GetDashboardUsecase
import com.trolla.healthsdk.feature_dashboard.domain.usecases.GetProfileUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.utils.TrollaHealthUtility
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getDashboardUsecase: GetDashboardUsecase,
    private val getProfileUsecase: GetProfileUsecase
) : BaseViewModel() {

    val dashboardResponseLiveData = MutableLiveData<Resource<BaseApiResponse<DashboardResponse>>>()

    fun getDashboard() {
        progressStatus.value = true
        viewModelScope.launch {
            dashboardResponseLiveData.value = getDashboardUsecase()!!
            progressStatus.value = false
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            val response = getProfileUsecase()!!

            response?.let {
                when (it) {
                    is Resource.Success -> {
                        TrollaPreferencesManager.put(
                            it?.data?.data,
                            TrollaPreferencesManager.USER_DATA
                        )
                    }

                    is Resource.Error -> {

                    }
                }
            }
        }
    }
}