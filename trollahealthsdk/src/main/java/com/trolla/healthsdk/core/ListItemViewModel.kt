package com.trolla.healthsdk.core

abstract class ListItemViewModel {
    var adapterPosition: Int = -1
    var onListItemViewClickListener: GenericAdapter.OnListItemViewClickListener? = null
}