package com.trolla.healthsdk.feature_prescriptionupload.data

import com.trolla.healthsdk.core.ListItemViewModel

data class ModelPrescription(
    val url: String,
    val isDeleteAvailable: Boolean = true
) : ListItemViewModel()
