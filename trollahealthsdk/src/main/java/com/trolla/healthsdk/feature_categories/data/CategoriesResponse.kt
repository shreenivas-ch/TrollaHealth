package com.trolla.healthsdk.feature_categories.data

import com.trolla.healthsdk.core.ListItemViewModel
import java.lang.ref.SoftReference

data class CategoriesResponse(
    val categories: ArrayList<Category>
) {
    data class Category(
        val wf_name: String,
        val wf_id: String,
        val wf_img: String,
        val HasSubCat1: String,
    ) : ListItemViewModel()
}