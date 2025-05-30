package com.jonpeps.gamescms.data.dataclasses.moshi

import com.squareup.moshi.Json

data class StringListMoshi(
    @Json(name = "items")
    var list: List<String>
)
