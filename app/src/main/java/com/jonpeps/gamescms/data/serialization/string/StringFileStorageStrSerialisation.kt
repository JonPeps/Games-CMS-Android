package com.jonpeps.gamescms.data.serialization.string

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

interface IStringFileStorageStrSerialisation {
    suspend fun write(directory: File,
                      mainFile: File,
                      absoluteFile: File,
                      fileWriter: FileWriter,
                      contents: String): Boolean

    suspend fun read(bufferedReader: BufferedReader): Boolean

    fun setContents(contents: String)
    fun getContents(): String
    fun getErrorMsg(): String
}

class StringFileStorageStrSerialisation
@Inject constructor(private val stringSerialization: IStringSerialization,
                    private val dispatcher: CoroutineDispatcher
) :
    IStringFileStorageStrSerialisation {
    private var contents: String = ""
    private var errorMsg: String = ""

    override suspend fun write(directory: File,
                               mainFile: File,
                               absoluteFile: File,
                               fileWriter: FileWriter,
                               contents: String): Boolean {
        this.contents = contents
        return withContext(dispatcher) {
            errorMsg = ""
            var success = true
            try {
                if (!directory.exists()) {
                    if (!directory.mkdir()) {
                        errorMsg = FAILED_TO_CREATE_DIRECTORY + directory.name
                        success = false
                    }
                }
                if (success) {
                    if (!absoluteFile.exists()) {
                        if (!absoluteFile.createNewFile()) {
                            errorMsg = FAILED_TO_CREATE_FILE + mainFile.name
                            success = false
                        }
                    }
                }
            } catch (ex: Exception) {
                errorMsg = ex.message.toString()
                success = false
            }
            if (!success) return@withContext false
            return@withContext stringSerialization.write(fileWriter, contents)
        }
    }

    override suspend fun read(bufferedReader: BufferedReader): Boolean {
        return withContext(dispatcher) {
            errorMsg = ""
            contents = ""
            var success = true
            if (stringSerialization.read(bufferedReader)) {
                contents = stringSerialization.getContents()
            } else {
                success = false
                errorMsg = stringSerialization.getErrorMsg()
            }
            return@withContext success
        }
    }

    override fun setContents(contents: String) {
        this.contents = contents
    }

    override fun getContents(): String = contents
    override fun getErrorMsg(): String = errorMsg

    companion object {
        const val FILE_DOES_NOT_EXIST = "File doesn't exist: "
        const val FAILED_TO_CREATE_DIRECTORY = "Failed to create directory: "
        const val FAILED_TO_CREATE_FILE = "Failed to create file: "
    }
}