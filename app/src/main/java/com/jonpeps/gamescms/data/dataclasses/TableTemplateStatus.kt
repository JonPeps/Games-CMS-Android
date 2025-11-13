package com.jonpeps.gamescms.data.dataclasses

data class TableTemplateStatus(
    val success: Boolean,
    val name: String,
    val errorMessage: String?,
)

data class TableTemplateStatusList(
    val items: List<TableTemplateStatus>
)