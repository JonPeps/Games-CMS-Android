package com.jonpeps.gamescms.data.serialization

import java.io.BufferedReader
import java.io.FileWriter

data class LoadStatus<T>(val status: Boolean, val message: String, val item: T?)
data class SaveStatus<T>(val status: Boolean, val message: String, val ex: T?)

interface IBaseSerialization<T, K> {
    suspend fun load(fileName: String, reader: BufferedReader)
    suspend fun save(fileName: String, item: K, writer: FileWriter)
    fun getLoadStatus(): LoadStatus<T>
    fun getSaveStatus(): SaveStatus<Exception>

}