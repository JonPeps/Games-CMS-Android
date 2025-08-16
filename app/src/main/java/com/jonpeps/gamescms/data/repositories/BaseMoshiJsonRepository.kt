package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.helpers.IBasicStringGenericItemCache
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.squareup.moshi.JsonAdapter
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter


interface MoshiJsonAdapter<T> {
    fun getJsonAdapter(): JsonAdapter<T>
}

interface IBaseMoshiJsonRepository<T> {
    suspend fun load(cacheName: String): Boolean
    suspend fun serialize(cacheName: String, contents: String): Boolean
    suspend fun save(cacheName: String, item: T): Boolean
    suspend fun delete(path: String, name: String): Boolean

    fun getItem(cacheName: String): T?
    fun getErrorMsg(): String

    fun setDirectoryFile(path: File)
    fun setFile(file: File)
    fun setAbsoluteFile(absoluteFile: File)
    fun setBufferReader(bufferedReader: BufferedReader)
    fun setFileWriter(fileWriter: FileWriter)

    fun setItem(cacheName: String, item: T?)
}

abstract class BaseMoshiJsonRepository<T>(
    private val moshiJsonAdapter: MoshiJsonAdapter<T>,
    private val stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
    private val basicStringGenericItemCache: IBasicStringGenericItemCache<T>
): IBaseMoshiJsonRepository<T> {
    protected lateinit var mainAbsolutePath: File
    protected var errorMessage = ""

    private var cacheName: String = ""
    private lateinit var directoryFile: File
    private lateinit var mainFile: File
    private lateinit var mainBufferedReader: BufferedReader
    private lateinit var mainFileWriter: FileWriter
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
        val jsonContents = moshiJsonAdapter.getJsonAdapter().fromJson(contents)
        return if (jsonContents != null) {
            basicStringGenericItemCache.set(cacheName, jsonContents)
            true
        } else {
            errorMessage = EMPTY_JSON_CONTENTS
            false
        }
    }

    override suspend fun delete(path: String, name: String): Boolean {
        return File(path + name).delete()
    }

    override fun getItem(cacheName: String): T? {
        return if (basicStringGenericItemCache.exists(cacheName)) {
            basicStringGenericItemCache.get(cacheName)
        } else {
            null
        }
    }

    override fun getErrorMsg(): String = errorMessage

    override fun setDirectoryFile(path: File) {
        directoryFile = path
    }
    override fun setFile(file: File) {
        mainFile = file
    }

    override fun setAbsoluteFile(absoluteFile: File) {
        mainAbsolutePath = absoluteFile
    }

    override fun setBufferReader(bufferedReader: BufferedReader) {
        mainBufferedReader = bufferedReader
    }

    override fun setFileWriter(fileWriter: FileWriter) {
        mainFileWriter = fileWriter
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