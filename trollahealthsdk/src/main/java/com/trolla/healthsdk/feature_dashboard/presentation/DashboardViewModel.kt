package com.trolla.healthsdk.feature_dashboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.feature_dashboard.domain.usecases.GetDashboardUsecase
import kotlinx.coroutines.launch

class DashboardViewModel(private val getDashboardUsecase: GetDashboardUsecase) : ViewModel() {

    fun getDashboard() {
        viewModelScope.launch {
            var result = getDashboardUsecase()
        }
    }
}