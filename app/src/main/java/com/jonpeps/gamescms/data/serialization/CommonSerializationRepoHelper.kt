package com.jonpeps.gamescms.data.serialization

import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

interface ICommonSerializationRepoHelper {
    fun getAbsoluteFile(directory: String, name: String): File
    fun getBufferReader(directory: String, name: String): BufferedReader
    fun getDirectoryFile(directory: String): File
    fun getMainFile(name: String): File
    fun getFileWriter(directory: String, name: String): FileWriter
}

class CommonSerializationRepoHelper@Inject constructor() : ICommonSerializationRepoHelper {
    @Throws(RuntimeException::class)
    override fun getAbsoluteFile(directory: String, name: String): File {
        if (directory.isEmpty() || name.isEmpty()) {
            throw RuntimeException("Directory or file name is null or empty!")
        }
        return File(getAbsolutePathName(directory, name))
    }

    override fun getBufferReader(directory: String, name: String): BufferedReader {
       return getAbsoluteFile(directory, name).bufferedReader()
    }

    override fun getDirectoryFile(directory: String): File {
        return File(directory)
    }

    override fun getMainFile(name: String): File {
        return File(name)
    }

    @Throws(RuntimeException::class)
    override fun getFileWriter(directory: String, name: String): FileWriter {
        if (directory.isEmpty() || name.isEmpty()) {
            throw RuntimeException("Directory or file name is null or empty!")
        }
        return FileWriter(getAbsoluteFile(directory, name))
    }

    companion object {
        fun getAbsolutePathName(path: String, fileName: String) = "$path$fileName"
    }
}