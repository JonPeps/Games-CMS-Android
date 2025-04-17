package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.serialization.moshi.IBaseMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter

interface IBaseMoshiJsonRepository<T> {
    suspend fun load(): Boolean
    suspend fun save(item: T): Boolean

    fun getItem(): T?
    fun getErrorMsg(): String

    fun setDirectoryFile(path: File)
    fun setFile(file: File)
    fun setAbsoluteFile(absoluteFile: File)
    fun setBufferReader(bufferedReader: BufferedReader)
    fun setFileWriter(fileWriter: FileWriter)
}

abstract class BaseMoshiJsonRepository<T>(private val stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
): IBaseMoshiJsonRepository<T> {
    private lateinit var directoryFile: File
    private lateinit var mainFile: File
    protected lateinit var mainAbsolutePath: File
    private lateinit var mainBufferedReader: BufferedReader
    private lateinit var mainFileWriter: FileWriter
    private var mainItem: T? = null
    protected var errorMessage = ""

    override suspend fun load(): Boolean {
        mainItem = null
        errorMessage = ""
        var success = true
        val moshiSerializer = getMoshiSerializer()
        val result = stringFileStorageStrSerialisation.read(mainAbsolutePath, mainBufferedReader)
        if (result) {
            val contents = stringFileStorageStrSerialisation.getContents()
            if(moshiSerializer.fromJson(contents)) {
                mainItem = moshiSerializer.getItem()
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

    override suspend fun save(item: T): Boolean {
        mainItem = item
        errorMessage = ""
        var success = false
        val moshiSerializer = getMoshiSerializer()
        if (moshiSerializer.toJson(item)) {
            val toJsonItem = moshiSerializer.getToJsonItem()
            if (toJsonItem == null) {
                errorMessage = moshiSerializer.getErrorMsg()
            } else {
                if (stringFileStorageStrSerialisation.write(directoryFile, mainFile, mainFileWriter, toJsonItem)) {
                    success = true
                } else {
                    errorMessage = stringFileStorageStrSerialisation.getErrorMsg()
                }
            }
        } else {
            errorMessage = moshiSerializer.getErrorMsg()
        }
        return success
    }

    override fun getItem(): T? = mainItem
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

    abstract fun getMoshiSerializer(): IBaseMoshiSerialization<T>
}