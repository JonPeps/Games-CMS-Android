package com.jonpeps.gamescms.ui.viewmodels

data class StringListToSplitItemListData(
    val success: Boolean,
    val names: List<String>,
    val fileNames: List<String>,
    val errorMessage: String = "",
    val exception: Exception? = null
)