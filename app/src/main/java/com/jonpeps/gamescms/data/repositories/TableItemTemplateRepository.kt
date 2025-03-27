package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.TableItemList
import com.jonpeps.gamescms.data.io.ISerializeString
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileReader
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
                val fileReader = FileReader(fileName)
                val jsonStr = serializeString.read(BufferedReader(fileReader))
                val tableItemList = jsonAdapter?.fromJson(jsonStr)
                return@withContext LoadTableItemTemplateStatus(true, "", tableItemList)
            } catch (ex: Exception) {
                return@withContext LoadTableItemTemplateStatus(false, ex.message.toString(), null)
            }
        }
    }

    override suspend fun save(fileName: String, tableItemList: TableItemList): SaveTableItemTemplateStatus {
        TODO("Not yet implemented")
    }
}