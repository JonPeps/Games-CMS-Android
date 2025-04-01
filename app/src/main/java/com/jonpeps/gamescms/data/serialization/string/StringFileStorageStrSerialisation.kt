package com.jonpeps.gamescms.data.serialization.string

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import javax.inject.Inject

interface IStrFileStorageWriteContents {
    fun getDirectory(): File
    fun getFile(): File
    fun getContents(): String
    fun getFileWriter(): FileWriter
}

interface IStrFileStorageReadContents {
    fun getAbsoluteFile(): File
    fun getBufferedReader(): BufferedReader
}

interface IStringFileStorageStrSerialisation {
    fun write(toWrite: IStrFileStorageWriteContents): Boolean
    fun read(toRead: IStrFileStorageReadContents): Boolean
    fun getContents(): String
    fun getErrorMsg(): String
}

class StringFileStorageStrSerialisation
@Inject constructor(private val stringSerialization: IStringSerialization) :
    IStringFileStorageStrSerialisation {
    private var contents: String = ""
    private var errorMsg: String = ""

    override fun write(toWrite: IStrFileStorageWriteContents): Boolean {
        errorMsg = ""
        val directory = toWrite.getDirectory()
        val file = toWrite.getFile()
        try {
            if (!directory.exists()) {
                if (!directory.mkdir()) {
                    errorMsg = FAILED_TO_CREATE_DIRECTORY + directory.name
                    return false
                }
            }
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    errorMsg = FAILED_TO_CREATE_FILE + file.name
                    return false
                }
            }
        } catch(ex: Exception) {
            errorMsg = ex.message.toString()
            return false
        }
        return stringSerialization.write(file.name, toWrite.getFileWriter(), toWrite.getContents())
    }

    override fun read(toRead: IStrFileStorageReadContents): Boolean {
        errorMsg = ""
        contents = ""
        val file = toRead.getAbsoluteFile()
        try {
            if (!file.exists()) {
                errorMsg = FILE_DOES_NOT_EXIST + file.name
                return false
            }
        } catch(ex: Exception) {
            errorMsg = ex.message.toString()
            return false
        }
        if (stringSerialization.read(toRead.getBufferedReader())) {
            contents = stringSerialization.getContents()
            return true
        }
        errorMsg = stringSerialization.getErrorMsg()
        return false
    }

    override fun getContents(): String = contents
    override fun getErrorMsg(): String = errorMsg

    companion object {
        const val FILE_DOES_NOT_EXIST = "File doesn't exist: "
        const val FAILED_TO_CREATE_DIRECTORY = "Failed to create directory: "
        const val FAILED_TO_CREATE_FILE = "Failed to create file: "
    }
}