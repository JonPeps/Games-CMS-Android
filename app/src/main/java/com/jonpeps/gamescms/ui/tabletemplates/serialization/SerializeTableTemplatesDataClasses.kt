package com.jonpeps.gamescms.ui.tabletemplates.serialization

import com.jonpeps.gamescms.data.dataclasses.TableTemplateStatus

data class SerializeTableTemplatesDataClasses(
    val success: Boolean,
    val names: List<String>,
    val fileNames: List<String>,
    val errorMessage: String = "",
    val exception: Exception? = null
)

data class SerializeTableTemplatesStatus(
    val success: Boolean,
    val items: List<TableTemplateStatus>?,
    val errorMessage: String = ""
)

data class SerializeUpdateTableTemplateStatus(
    val success: Boolean,
    val errorMessage: String = ""
)

data class SerializeTableTemplateUpdateCoreStatus(
    val success: Boolean,
    val overwrittenFilename: Boolean,
    val names: List<String>,
    val fileNames: List<String>,
    val templateFilename: String,
    val errorMessage: String = ""
)