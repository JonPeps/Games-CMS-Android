package com.jonpeps.gamescms.viewmodels.dynamodbservices

class TableRequestViewModelResponse<T>(
    val success: Boolean? = false,
    val response: T? = null,
    val exception: Exception? = null)