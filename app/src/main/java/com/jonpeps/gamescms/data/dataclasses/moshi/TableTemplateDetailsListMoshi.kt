package com.jonpeps.gamescms.data.dataclasses.moshi

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TableTemplateDetailsListMoshi(
    val items: ArrayList<TableTemplateDetailsMoshi>
)