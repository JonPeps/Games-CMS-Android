package com.jonpeps.gamescms.data.dataclasses.moshi

import com.squareup.moshi.Json

data class TableTemplateItemListMoshi(
    @Json(name = "template_name")
    val templateName: String,
    @Json(name = "items")
    var items: List<TableTemplateItemMoshi> = arrayListOf()
)