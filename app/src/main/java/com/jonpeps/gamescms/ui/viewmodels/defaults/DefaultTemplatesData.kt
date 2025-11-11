package com.jonpeps.gamescms.ui.viewmodels.defaults

data class StringListToSplitItemListData(
    val success: Boolean,
    val names: List<String>?,
    val fileNames: List<String>?,
    val errorMessage: String?,
    val exception: Exception?
)

data class DefaultTemplateStatus(
    val success: Boolean,
    val name: String,
    val errorMessage: String?,
)

data class DefaultTemplateStatusList(
    val items: List<DefaultTemplateStatus>
)