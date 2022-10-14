package com.trolla.healthsdk.feature_search.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.trolla.healthsdk.data.Resource
import com.trolla.healthsdk.data.models.BaseApiResponse
import com.trolla.healthsdk.feature_search.data.ModelSearchHistory
import com.trolla.healthsdk.feature_search.data.SearchResponse
import com.trolla.healthsdk.feature_search.domain.usecases.SearchUsecase
import com.trolla.healthsdk.ui_utils.BaseViewModel
import com.trolla.healthsdk.utils.TrollaPreferencesManager
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


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
            var localSearchHistoryStr =
                TrollaPreferencesManager.getString(
                    TrollaPreferencesManager.PM_LOCAL_SEARCH_HISTORY
                )

            if (localSearchHistoryStr.isNullOrEmpty()) {
                localSearchHistoryLiveData.value = arrayListOf()
            } else {
                val jsonArray = JSONArray(localSearchHistoryStr)
                var arr = ArrayList<ModelSearchHistory>()

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    arr.add(
                        ModelSearchHistory(
                            jsonObject.getInt("id"),
                            jsonObject.getString("title")
                        )
                    )
                }

                localSearchHistoryLiveData.value = arr
            }

        }
    }

    fun addSearchToLocalSearchHistory(dashboardProduct: ModelSearchHistory) {
        viewModelScope.launch {
            var arr = localSearchHistoryLiveData.value ?: arrayListOf()
            arr.add(dashboardProduct)

            var jsonArr = JSONArray()
            for (i in arr) {
                var jsonObj = JSONObject()
                jsonObj.put("id", i.id)
                jsonObj.put("title", i.title)
                jsonArr.put(jsonObj)
            }

            TrollaPreferencesManager.setString(
                jsonArr.toString(),
                TrollaPreferencesManager.PM_LOCAL_SEARCH_HISTORY
            )
        }
    }
}