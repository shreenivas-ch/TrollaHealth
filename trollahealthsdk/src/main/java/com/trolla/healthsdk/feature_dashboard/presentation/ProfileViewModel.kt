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
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel() {

    val profileNameLiveData = MutableLiveData<String>()
    val profileEmailLiveData = MutableLiveData<String>()
    val profileMobileLiveData = MutableLiveData<String>()
    fun getProfile() {

        viewModelScope.launch {
            profileNameLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_NAME)
            profileEmailLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_EMAIL)
            profileMobileLiveData.value =
                TrollaPreferencesManager.getString(TrollaPreferencesManager.PROFILE_MOBILE)
        }
    }

}