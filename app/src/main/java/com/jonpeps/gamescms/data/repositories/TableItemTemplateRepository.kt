package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.jonpeps.gamescms.data.core.ISerializeString
import com.jonpeps.gamescms.data.core.SerializeStringStatus
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import javax.inject.Inject

data class LoadTableItemTemplateStatus(val status: Boolean, val message: String, val items: TableItemList?)
data class SaveTableItemTemplateStatus(val status: Boolean, val message: String)

interface ITableItemTemplateRepository {
    suspend fun load(fileName: String): LoadTableItemTemplateStatus
    suspend fun save(fileName: String, tableItemList: TableItemList): SaveTableItemTemplateStatus
}

class TableItemTemplateRepository
@Inject constructor(private val serializeString: ISerializeString,
                    private val dispatcher: CoroutineDispatcher
) : ITableItemTemplateRepository {
    private var jsonAdapter: JsonAdapter<TableItemList>? = null

    init {
        val moshi = Moshi.Builder().build()
        jsonAdapter = moshi.adapter(TableItemList::class.java)
    }

    override suspend fun load(fileName: String): LoadTableItemTemplateStatus {
        return withContext(dispatcher) {
            try {
                var message = ""
                var success = true
                val fileReader = FileReader(fileName)
                val jsonStr = serializeString.read(BufferedReader(fileReader))
                val readStatus = serializeString.getStatus()
                if (readStatus.status != SerializeStringStatus.SUCCESS) {
                    success = false
                    message = readStatus.message
                } else if (jsonStr.isEmpty()) {
                    success = false
                    message = "Failed to read from file!"
                }
                val tableItemList = jsonAdapter?.fromJson(jsonStr)
                return@withContext LoadTableItemTemplateStatus(success, message, tableItemList)
            } catch (ex: Exception) {
                return@withContext LoadTableItemTemplateStatus(false, ex.message.toString(), null)
            }
        }
    }

    override suspend fun save(fileName: String, tableItemList: TableItemList): SaveTableItemTemplateStatus {
        return withContext(dispatcher) {
            try {
                var message = ""
                var success = true
                val fileWriter = FileWriter(fileName)
                val jsonStr = jsonAdapter?.toJson(tableItemList)
                if (jsonStr.isNullOrEmpty()) {
                    success = false
                    message = "Failed to write to file!"
                } else {
                    serializeString.write(fileName, fileWriter, jsonStr.toString())
                    val writeStatus = serializeString.getStatus()
                    if (writeStatus.status != SerializeStringStatus.SUCCESS) {
                        success = false
                        message = writeStatus.message
                    }
                }
                return@withContext SaveTableItemTemplateStatus(success, message)
            } catch (ex: Exception) {
                return@withContext SaveTableItemTemplateStatus(false, ex.message.toString())
            }
        }
    }
}