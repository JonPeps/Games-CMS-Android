package com.jonpeps.gamescms.data.serialization.moshi

interface IBaseMoshiSerialization<T> {
    suspend fun fromJson(contents: String): Boolean
    suspend fun toJson(item: T): Boolean
    fun getItem(): T?
    fun getToJsonItem(): String?
    fun getErrorMsg(): String
}