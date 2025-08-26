package com.jonpeps.gamescms.data.dataclasses.moshi

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StringListMoshi(
    val items: List<String>
)
