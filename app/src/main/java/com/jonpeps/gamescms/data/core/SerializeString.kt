package com.jonpeps.gamescms.data.core

import java.io.BufferedReader
import java.io.FileWriter
import java.io.IOException
import java.io.UncheckedIOException

enum class SerializeStringStatus {
    SUCCESS,
    WRITE_FAILED,
    READ_FAILED
}

data class SerializeStringStatusGroup(val status: SerializeStringStatus, val message: String)

interface ISerializeString {
    fun write(fileName: String, fileWriter: FileWriter, fileContents: String)
    fun read(bufferedReader: BufferedReader): String
    fun getStatus(): SerializeStringStatusGroup
}

class SerializeString : ISerializeString {
    private lateinit var status: SerializeStringStatusGroup

    override fun write(fileName: String, fileWriter: FileWriter, fileContents: String) {
        status = SerializeStringStatusGroup(SerializeStringStatus.SUCCESS, "")
        try {
            fileWriter.write(fileContents)
        } catch (ex: IOException) {
            status = SerializeStringStatusGroup(SerializeStringStatus.WRITE_FAILED, ex.message.toString())
        }
        fileWriter.close()
    }

    override fun read(bufferedReader: BufferedReader): String {
        status = SerializeStringStatusGroup(SerializeStringStatus.SUCCESS, "")
        val stringBuilder = StringBuilder()
        try {
            bufferedReader.lines().forEach {
                stringBuilder.append(it)
            }
        } catch (ex: UncheckedIOException) {
            status = SerializeStringStatusGroup(SerializeStringStatus.READ_FAILED, UNCHECKED_IO_MESSAGE)
        }
        bufferedReader.close()
        return stringBuilder.toString()
    }

    override fun getStatus(): SerializeStringStatusGroup = status

    companion object {
        const val UNCHECKED_IO_MESSAGE = "Failed to read from String buffer!"
    }
}