package com.jonpeps.gamescms.data.core

import java.io.BufferedReader
import java.io.FileWriter
import java.io.IOException
import java.io.UncheckedIOException

enum class IoSerializationStringStatus {
    SUCCESS,
    WRITE_FAILED,
    READ_FAILED
}

data class SerializeStringStatusGroup(val status: IoSerializationStringStatus, val message: String)

interface IStringSerialization {
    fun write(fileName: String, fileWriter: FileWriter, fileContents: String)
    fun read(bufferedReader: BufferedReader): String
    fun getStatus(): SerializeStringStatusGroup
}

class StringSerialization : IStringSerialization {
    private lateinit var status: SerializeStringStatusGroup

    override fun write(fileName: String, fileWriter: FileWriter, fileContents: String) {
        status = SerializeStringStatusGroup(IoSerializationStringStatus.SUCCESS, "")
        try {
            fileWriter.write(fileContents)
        } catch (ex: IOException) {
            status = SerializeStringStatusGroup(IoSerializationStringStatus.WRITE_FAILED, ex.message.toString())
        }
        fileWriter.close()
    }

    override fun read(bufferedReader: BufferedReader): String {
        status = SerializeStringStatusGroup(IoSerializationStringStatus.SUCCESS, "")
        val stringBuilder = StringBuilder()
        try {
            bufferedReader.lines().forEach {
                stringBuilder.append(it)
            }
        } catch (ex: UncheckedIOException) {
            status = SerializeStringStatusGroup(IoSerializationStringStatus.READ_FAILED, UNCHECKED_IO_MESSAGE)
        }
        bufferedReader.close()
        return stringBuilder.toString()
    }

    override fun getStatus(): SerializeStringStatusGroup = status

    companion object {
        const val UNCHECKED_IO_MESSAGE = "Failed to read from String buffer!"
    }
}