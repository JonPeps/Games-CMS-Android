package com.jonpeps.gamescms.data

import com.squareup.moshi.Json

data class TableItemList(
    @Json(name = "template_name")
    val templateName: String,
    @Json(name = "items")
    var items: List<TableItem> = arrayListOf()
)