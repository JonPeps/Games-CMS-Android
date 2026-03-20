package com.jonpeps.gamescms.data.repositories.base

import com.squareup.moshi.JsonAdapter

interface MoshiJsonAdapter<T> {
    fun getJsonAdapter(): JsonAdapter<T>
}