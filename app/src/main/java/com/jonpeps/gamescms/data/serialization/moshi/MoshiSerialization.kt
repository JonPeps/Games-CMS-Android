package com.jonpeps.gamescms.data.serialization.moshi

import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException

abstract class MoshiSerialization<T>(private val dispatcher: CoroutineDispatcher
) : IBaseMoshiSerialization<T> {
    private var item: T? = null
    private var jsonItem: String = ""
    private var errorMsg: String = ""

    override suspend fun fromJson(contents: String): Boolean {
        item = null
        errorMsg = ""
        return withContext(dispatcher) {
            var success = false
            try {
                item = getMoshiAdapter().fromJson(contents)
                success = true
            } catch (ex : IOException) {
                errorMsg = ex.message.toString()
            }
            return@withContext success
        }
    }

    override suspend fun toJson(item: T): Boolean {
        errorMsg = ""
        return withContext(dispatcher) {
            var success: Boolean
            try {
                jsonItem = getMoshiAdapter().toJson(item)
                if (jsonItem == null || jsonItem.isEmpty()) {
                    success = false
                    errorMsg = CONVERT_TO_JSON_ERROR_MESSAGE + item.toString()
                } else {
                    success = true
                }
            } catch (ex: AssertionError) {
                success = false
                errorMsg = ex.message.toString()
            }
            return@withContext success
        }
    }

    abstract fun getMoshiAdapter(): JsonAdapter<T>
    override fun getItem(): T? = item
    override fun getToJsonItem(): String = jsonItem
    override fun getErrorMsg(): String = errorMsg

    companion object {
        const val CONVERT_TO_JSON_ERROR_MESSAGE = "Failed to convert item to Json: "
    }
}