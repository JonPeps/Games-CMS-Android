package com.jonpeps.gamescms.data.repositories

import java.io.BufferedReader
import java.io.File
import java.io.FileWriter

interface IBaseMoshiRepo {
    suspend fun delete(path: String, name: String): Boolean

    fun assignDirectoryFile(path: File)
    fun setFile(file: File)
    fun setAbsoluteFile(absoluteFile: File)
    fun setBufferReader(bufferedReader: BufferedReader)
    fun setFileWriter(fileWriter: FileWriter)

    fun getErrorMsg(): String
}

abstract class BaseMoshiRepo: IBaseMoshiRepo {
    protected var errorMessage = ""
    protected lateinit var directoryFile: File
    protected lateinit var mainFile: File
    protected lateinit var mainAbsolutePath: File
    protected lateinit var mainBufferedReader: BufferedReader
    protected lateinit var mainFileWriter: FileWriter

    override suspend fun delete(path: String, name: String): Boolean {
        return File(path + name).delete()
    }

    override fun getErrorMsg(): String = errorMessage

    override fun assignDirectoryFile(path: File) {
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

    companion object {
        const val EMPTY_JSON_CONTENTS = "Failed to parse JSON content!"
        const val CONVERT_TO_JSON_FAILED = "Failed to convert to JSON!"
        const val WRITE_TO_FILE_FAILED = "Failed to write to file!"
    }
}