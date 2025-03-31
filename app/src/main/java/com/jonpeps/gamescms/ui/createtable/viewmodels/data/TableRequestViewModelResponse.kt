package com.jonpeps.gamescms.ui.createtable.viewmodels.data

class TableRequestViewModelResponse<T>(
    val success: Boolean = false,
    val response: T? = null,
    val exception: Exception? = null)