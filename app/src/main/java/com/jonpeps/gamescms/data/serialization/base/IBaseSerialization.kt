package com.jonpeps.gamescms.data.serialization.base

import java.io.BufferedReader
import java.io.FileWriter

interface IBaseSerialization<T> {
    suspend fun load(fileName: String, reader: BufferedReader): Boolean
    suspend fun save(fileName: String, item: T, writer: FileWriter): Boolean
    fun getItem(): T?
    fun getErrorMsg(): String
}