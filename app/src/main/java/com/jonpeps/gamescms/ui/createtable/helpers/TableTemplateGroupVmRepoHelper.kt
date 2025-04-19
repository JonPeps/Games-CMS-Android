package com.jonpeps.gamescms.ui.createtable.helpers

import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

interface ITableTemplateGroupVmRepoHelper {
    fun getAbsoluteFile(directory: String, templateName: String): File
    fun getBufferReader(directory: String, templateName: String): BufferedReader
    fun getDirectoryFile(directory: String): File
    fun getMainFile(templateName: String): File
    fun getFileWriter(directory: String, templateName: String): FileWriter
}

class TableTemplateGroupVmRepoHelper@Inject constructor() : ITableTemplateGroupVmRepoHelper {
    @Throws(RuntimeException::class)
    override fun getAbsoluteFile(directory: String, templateName: String): File {
        if (directory.isEmpty() || templateName.isEmpty()) {
            throw RuntimeException("Directory or template name is null or empty!")
        }
        return File(getAbsolutePathName(directory, templateName))
    }

    override fun getBufferReader(directory: String, templateName: String): BufferedReader {
       return getAbsoluteFile(directory, templateName).bufferedReader()
    }

    override fun getDirectoryFile(directory: String): File {
        return File(directory)
    }

    override fun getMainFile(templateName: String): File {
        return File(templateName)
    }

    @Throws(RuntimeException::class)
    override fun getFileWriter(directory: String, templateName: String): FileWriter {
        if (directory.isEmpty() || templateName.isEmpty()) {
            throw RuntimeException("Directory or template name is null or empty!")
        }
        return FileWriter(getAbsoluteFile(directory, templateName))
    }

    companion object {
        private const val FILE_EXTENSION = ".json"

        fun getAbsolutePathName(path: String, fileName: String) = "$path$fileName$FILE_EXTENSION"
    }
}