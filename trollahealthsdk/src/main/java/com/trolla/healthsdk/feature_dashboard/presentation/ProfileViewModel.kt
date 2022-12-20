package com.trolla.healthsdk.feature_dashboard.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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