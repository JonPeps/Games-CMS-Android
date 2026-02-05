package com.jonpeps.gamescms.data.dataclasses

data class CommonDataItem<T>(
    val success: Boolean,
    val item: T?,
    val currentIndex: Int,
    val message: String?,
    val ex: Exception?
)