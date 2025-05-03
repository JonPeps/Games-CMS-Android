package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.helpers.IBasicStringGenericItemCache
import com.jonpeps.gamescms.data.serialization.moshi.IBaseMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter

interface IBaseMoshiJsonRepository<T> {
    suspend fun load(cacheName: String): Boolean
    suspend fun save(cacheName: String, item: T): Boolean

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
        var success = true
        val moshiSerializer = getMoshiSerializer()
        val result = stringFileStorageStrSerialisation.read(mainAbsolutePath, mainBufferedReader)
        if (result) {
            val contents = stringFileStorageStrSerialisation.getContents()
            if(moshiSerializer.fromJson(contents)) {
                basicStringGenericItemCache.set(cacheName, moshiSerializer.getItem())
            } else {
                errorMessage = moshiSerializer.getErrorMsg()
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
        val moshiSerializer = getMoshiSerializer()
        if (moshiSerializer.toJson(item)) {
            val toJsonItem = moshiSerializer.getToJsonItem()
            if (toJsonItem != null) {
                success = stringFileStorageStrSerialisation
                    .write(directoryFile,
                           mainFile,
                           mainAbsolutePath,
                           mainFileWriter,
                           toJsonItem)
                if (!success) {
                    errorMessage = stringFileStorageStrSerialisation.getErrorMsg()
                    return false
                }
            }
        }
        if (success) {
            basicStringGenericItemCache.set(cacheName, item)
        } else {
            errorMessage = moshiSerializer.getErrorMsg()
        }
        return success
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

    abstract fun getMoshiSerializer(): IBaseMoshiSerialization<T>
}