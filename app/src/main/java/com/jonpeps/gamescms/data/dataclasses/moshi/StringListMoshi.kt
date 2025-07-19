package com.jonpeps.gamescms.data.dataclasses.moshi

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StringListMoshi(
    @field:Json(name = "items")
    var list: List<String>
)
