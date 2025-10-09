package com.jonpeps.gamescms.data.dataclasses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TableItemFinalList(
    val items: List<TableItemFinal>
)
