package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.serialization.string.IStringSerialization
import com.jonpeps.gamescms.data.serialization.base.IBaseSerialization
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileWriter
import java.io.IOException

abstract class MoshiSerialization<T>(private val serializeString: IStringSerialization,
                                     private val dispatcher: CoroutineDispatcher
) : IBaseSerialization<T> {
    private var item: T? = null
    private var errorMsg: String = ""

    override suspend fun load(fileName: String, reader: BufferedReader): Boolean {
        return withContext(dispatcher) {
            item = null
            var success = false
            if (serializeString.read(reader)) {
                try {
                    item = getMoshiAdapter().fromJson(serializeString.getContents())
                    success = true
                } catch (ex : IOException) {
                    errorMsg = ex.message.toString()
                }
            } else {
                errorMsg = serializeString.getErrorMsg()
            }
            return@withContext success
        }
    }

    override suspend fun save(fileName: String, item: T, writer: FileWriter): Boolean {
        return withContext(dispatcher) {
            var success: Boolean
            errorMsg = ""
            try {
                val jsonStr = getMoshiAdapter().toJson(item)
                if (jsonStr.isNullOrEmpty()) {
                    success = false
                    errorMsg = "Failed to write to file $fileName!"
                } else {
                    if (serializeString.write(fileName, writer, jsonStr.toString())) {
                        success = true
                    } else {
                        success = false
                        errorMsg = serializeString.getErrorMsg()
                    }
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
    override fun getErrorMsg(): String = errorMsg
}