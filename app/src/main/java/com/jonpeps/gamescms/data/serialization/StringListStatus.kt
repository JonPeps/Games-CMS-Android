package com.jonpeps.gamescms.data.serialization

data class StringListStatus(
    val success: Boolean,
    val items: ArrayList<String>,
    val message: String?,
    val ex: Exception?)