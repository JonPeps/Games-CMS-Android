package com.jonpeps.gamescms.viewmodels.dynamodbservices.data

class TableRequestViewModelResponse<T>(
    val success: Boolean? = false,
    val response: T? = null,
    val exception: Exception? = null)