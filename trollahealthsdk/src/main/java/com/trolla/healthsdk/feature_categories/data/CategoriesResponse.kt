package com.trolla.healthsdk.feature_categories.data

import com.trolla.healthsdk.core.ListItemViewModel
import java.lang.ref.SoftReference

data class CategoriesResponse(
    val categories: ArrayList<Category>
) {
    data class Category(
        val category_name: String,
        val category_id: String,
        val category_img: String,
        val HasSubCat1: String,
    ) : ListItemViewModel()
}