package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.core.IStringSerialization
import com.jonpeps.gamescms.data.core.IoSerializationStringStatus
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileWriter
import java.io.IOException
import javax.inject.Inject

abstract class MoshiSerialization<T>
@Inject constructor(private val serializeString: IStringSerialization,
                    private val dispatcher: CoroutineDispatcher
) : IBaseSerialization<T, T> {
    private lateinit var loadRepositoryStatus: LoadStatus<T>
    private lateinit var saveRepositoryStatus: SaveStatus<Exception>

    override suspend fun load(fileName: String, reader: BufferedReader) {
        return withContext(dispatcher) {
            var message = ""
            var success = true
            var tableItemList: T? = null
            try {
                val jsonStr = serializeString.read(reader)
                val readStatus = serializeString.getStatus()
                if (readStatus.status != IoSerializationStringStatus.SUCCESS) {
                    success = false
                    message = readStatus.message
                } else if (jsonStr.isEmpty()) {
                    success = false
                    message = "Failed to read from file $fileName!"
                } else {
                    try {
                        tableItemList = getMoshiAdapter().fromJson(jsonStr)
                    } catch (ex : IOException) {
                        success = false
                        message = ex.message.toString()
                    }
                }
            } catch (ex: Exception) {
                success = false
                message = ex.message.toString()
            }
            loadRepositoryStatus = LoadStatus(success, message, tableItemList)
            return@withContext
        }
    }

    override suspend fun save(fileName: String, item: T, writer: FileWriter) {
        return withContext(dispatcher) {
            var message = ""
            var success = true
            var exception: Exception? = null
            try {
                val jsonStr = getMoshiAdapter().toJson(item)
                if (jsonStr.isNullOrEmpty()) {
                    success = false
                    message = "Failed to write to file $fileName!"
                } else {
                    serializeString.write(fileName, writer, jsonStr.toString())
                    val writeStatus = serializeString.getStatus()
                    if (writeStatus.status != IoSerializationStringStatus.SUCCESS) {
                        success = false
                        message = writeStatus.message
                    }
                }
            } catch (ex: Exception) {
                success = false
                message = ex.message.toString()
                exception = ex
            }
            saveRepositoryStatus = SaveStatus(success, message, exception)
            return@withContext
        }
    }

    abstract fun getMoshiAdapter(): JsonAdapter<T>

    override fun getLoadStatus(): LoadStatus<T> = loadRepositoryStatus
    override fun getSaveStatus(): SaveStatus<Exception> = saveRepositoryStatus
}