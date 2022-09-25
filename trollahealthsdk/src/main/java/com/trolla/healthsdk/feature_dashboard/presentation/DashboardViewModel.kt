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
                            it?.data?.data?.userData?._id,
                            TrollaPreferencesManager.PROFILE_ID
                        )
                        TrollaPreferencesManager.put(
                            it?.data?.data?.userData?.name,
                            TrollaPreferencesManager.PROFILE_NAME
                        )
                        TrollaPreferencesManager.put(
                            it?.data?.data?.userData?.email,
                            TrollaPreferencesManager.PROFILE_EMAIL
                        )
                        TrollaPreferencesManager.put(
                            it?.data?.data?.userData?.mobile,
                            TrollaPreferencesManager.PROFILE_MOBILE
                        )
                        TrollaPreferencesManager.put(
                            it?.data?.data?.userData?.gender,
                            TrollaPreferencesManager.PROFILE_GENDER
                        )
                        TrollaPreferencesManager.put(
                            it?.data?.data?.userData?.day,
                            TrollaPreferencesManager.PROFILE_DAY
                        )
                        TrollaPreferencesManager.put(
                            it?.data?.data?.userData?.month,
                            TrollaPreferencesManager.PROFILE_MONTH
                        )
                        TrollaPreferencesManager.put(
                            it?.data?.data?.userData?.year,
                            TrollaPreferencesManager.PROFILE_YEAR
                        )
                    }

                    is Resource.Error -> {

                    }
                }
            }
        }
    }
}