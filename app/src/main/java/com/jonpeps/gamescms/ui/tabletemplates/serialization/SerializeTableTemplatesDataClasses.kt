package com.jonpeps.gamescms.ui.tabletemplates.serialization

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi

data class SerializeTableTemplateUpdateCoreStatus(
    val success: Boolean,
    val overwrittenFilename: Boolean,
    val names: List<String>,
    val fileNames: List<String>,
    val templateFilename: String,
    val errorMessage: String = ""
)

data class SerializeTableTemplatesViewModelData(
    val success: Boolean,
    val results: TableTemplateDetailsListMoshi?,
    val errorMessage: String = ""
)

data class UpdatedTableTemplatesViewModelData(val success: Boolean, val errorMessage: String = "")