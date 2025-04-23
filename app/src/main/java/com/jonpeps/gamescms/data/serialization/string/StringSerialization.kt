package com.jonpeps.gamescms.data.serialization.string

import java.io.BufferedReader
import java.io.FileWriter
import java.io.IOException
import java.io.UncheckedIOException

interface IStringSerialization {
    fun write(fileWriter: FileWriter, fileContents: String): Boolean
    fun read(bufferedReader: BufferedReader): Boolean
    fun getContents(): String
    fun getErrorMsg(): String
}

class StringSerialization : IStringSerialization {
    private var contents: String = ""
    private var errorMsg: String = ""

    override fun write(fileWriter: FileWriter, fileContents: String): Boolean {
        var success = true
        errorMsg = ""
        try {
            if (fileContents.isEmpty()) {
                errorMsg = CONTENTS_IS_NULL_OR_EMPTY
                return false
            }
            fileWriter.write(fileContents)
        } catch (ex: IOException) {
            errorMsg = ex.toString()
            success = false
        }
        fileWriter.close()
        return success
    }

    override fun read(bufferedReader: BufferedReader): Boolean {
        var success = true
        errorMsg = ""
        contents = ""
        val stringBuilder = StringBuilder()
        try {
            bufferedReader.lines().forEach {
                stringBuilder.append(it)
            }
            contents = stringBuilder.toString()
        } catch (ex: UncheckedIOException) {
            success = false
            errorMsg = UNCHECKED_IO_MESSAGE
        }
        bufferedReader.close()
        return success
    }

    override fun getContents(): String = contents
    override fun getErrorMsg(): String = errorMsg

    companion object {
        const val UNCHECKED_IO_MESSAGE = "Failed to read from String buffer!"
        const val CONTENTS_IS_NULL_OR_EMPTY = "File contents is null or empty!"
    }
}