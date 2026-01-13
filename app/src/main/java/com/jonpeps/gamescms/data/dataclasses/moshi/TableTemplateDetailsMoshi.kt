package com.jonpeps.gamescms.data.dataclasses.moshi

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TableTemplateDetailsMoshi(val name: String, val filename: String, val status: Boolean)