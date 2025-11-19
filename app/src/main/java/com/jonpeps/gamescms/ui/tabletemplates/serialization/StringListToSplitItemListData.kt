package com.jonpeps.gamescms.ui.tabletemplates.serialization

data class StringListToSplitItemListData(
    val success: Boolean,
    val names: List<String>,
    val fileNames: List<String>,
    val errorMessage: String = "",
    val exception: Exception? = null
)