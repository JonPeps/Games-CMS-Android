package com.jonpeps.gamescms.data.dataclasses

data class StatusData(
    val name: String,
    val isValid: Boolean,
    val exceptionMsg: String?,
    val message: String?
)
