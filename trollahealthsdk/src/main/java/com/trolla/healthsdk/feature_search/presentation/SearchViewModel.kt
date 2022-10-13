package com.trolla.healthsdk.feature_search.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_categories.data.CategoriesResponse
import com.trolla.healthsdk.feature_categories.domain.usecases.GetCategoriesUsecase
import com.trolla.healthsdk.feature_dashboard.data.DashboardResponse
import com.trolla.healthsdk.feature_search.data.ModelSearchHistory
import com.trolla.healthsdk.feature_search.data.SearchResponse
import com.trolla.healthsdk.feature_search.domain.usecases.SearchUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import kotlinx.coroutines.launch

class SearchViewModel(private val searchUsecase: SearchUsecase) :
    BaseViewModel() {

    val searchResponseLiveData =
        MutableLiveData<Resource<BaseApiResponse<SearchResponse>>>()

    val localSearchHistoryLiveData =
        MutableLiveData<ArrayList<ModelSearchHistory>>()

    fun search(query: String, page: String, limit: String) {
        progressStatus.value = true
        viewModelScope.launch {
            searchResponseLiveData.value = searchUsecase(query, page, limit)!!
            progressStatus.value = false
        }
    }

    fun getLocalSearchHistory() {
        viewModelScope.launch {
            var localSearchHistory =
                TrollaPreferencesManager.get<ArrayList<ModelSearchHistory>>(
                    TrollaPreferencesManager.PM_LOCAL_SEARCH_HISTORY
                )
                    ?: arrayListOf()

            if (localSearchHistory.isNullOrEmpty()) {
                localSearchHistoryLiveData.value = arrayListOf()
            } else {
                localSearchHistoryLiveData.value = localSearchHistory
            }

        }
    }

    fun addSearchToLocalSearchHistory(dashboardProduct: ModelSearchHistory) {
        viewModelScope.launch {
            var arr = localSearchHistoryLiveData.value ?: arrayListOf()
            arr.add(dashboardProduct)
            TrollaPreferencesManager.put(arr, TrollaPreferencesManager.PM_LOCAL_SEARCH_HISTORY)
        }
    }
}