package com.jonpeps.gamescms.data.dataclasses.moshi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TableTemplateItemListMoshi(
    @field:Json(name = "template_name")
    val templateName: String,
    @field:Json(name = "items")
    var items: List<TableTemplateItemMoshi> = arrayListOf()
)