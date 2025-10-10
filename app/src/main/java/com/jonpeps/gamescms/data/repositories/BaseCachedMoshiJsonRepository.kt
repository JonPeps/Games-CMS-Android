package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.helpers.IBasicStringGenericItemCache
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation

interface IBaseCachedMoshiJsonRepository<T>: IBaseMoshiRepo {
    suspend fun load(cacheName: String): Boolean
    suspend fun serialize(cacheName: String, contents: String): Boolean
    suspend fun save(cacheName: String, item: T): Boolean

    fun getItem(cacheName: String): T?
    fun setItem(cacheName: String, item: T?)
}

abstract class BaseCachedMoshiJsonRepository<T>(
    private val moshiJsonAdapter: MoshiJsonAdapter<T>,
    private val stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
    private val basicStringGenericItemCache: IBasicStringGenericItemCache<T>
): BaseMoshiRepo(), IBaseCachedMoshiJsonRepository<T> {
    private val cacheMap = mutableMapOf<String, T>()

    override suspend fun load(cacheName: String): Boolean {
        errorMessage = ""
        var success = stringFileStorageStrSerialisation
            .read(mainBufferedReader)
        if (success) {
            val contents = stringFileStorageStrSerialisation.getContents()
            val jsonContents = moshiJsonAdapter.getJsonAdapter().fromJson(contents)
            if(jsonContents != null) {
                basicStringGenericItemCache.set(cacheName, jsonContents)
            } else {
                errorMessage = EMPTY_JSON_CONTENTS
                success = false
            }
        } else {
            errorMessage = stringFileStorageStrSerialisation.getErrorMsg()
            success = false
        }
        return success
    }

    override suspend fun save(cacheName: String, item: T): Boolean {
        errorMessage = ""
        var success = false
        val jsonContents = moshiJsonAdapter.getJsonAdapter().toJson(item)
        if (jsonContents.isNullOrEmpty()) {
            errorMessage = CONVERT_TO_JSON_FAILED
        } else {
            success = stringFileStorageStrSerialisation.write(directoryFile,
                                                              mainFile,
                                                              mainAbsolutePath,
                                                              mainFileWriter,
                                                              jsonContents)
            if (success) {
                basicStringGenericItemCache.set(cacheName, item)
            } else {
                errorMessage = WRITE_TO_FILE_FAILED
            }
        }
        return success
    }

    override suspend fun serialize(cacheName: String, contents: String): Boolean {
        errorMessage = ""
        val jsonAdapter = moshiJsonAdapter.getJsonAdapter()
        val jsonContents = jsonAdapter.fromJson(contents)
        return if (jsonContents != null) {
            basicStringGenericItemCache.set(cacheName, jsonContents)
            true
        } else {
            errorMessage = EMPTY_JSON_CONTENTS
            false
        }
    }

    override fun getItem(cacheName: String): T? {
        return if (basicStringGenericItemCache.exists(cacheName)) {
            basicStringGenericItemCache.get(cacheName)
        } else {
            null
        }
    }

    override fun setItem(cacheName: String, item: T?) {
        if (item != null) {
            cacheMap[cacheName] = item
        }
    }

    companion object {
        const val EMPTY_JSON_CONTENTS = "Failed to parse JSON content!"
        const val CONVERT_TO_JSON_FAILED = "Failed to convert to JSON!"
        const val WRITE_TO_FILE_FAILED = "Failed to write to file!"
    }
}