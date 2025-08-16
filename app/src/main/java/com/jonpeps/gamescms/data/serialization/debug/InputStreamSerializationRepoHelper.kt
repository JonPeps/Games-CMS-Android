package com.jonpeps.gamescms.data.serialization.debug

import java.io.BufferedReader
import java.io.InputStream
import javax.inject.Inject

interface IInputStreamSerializationRepoHelper {
    fun getBufferReader(inputStream: InputStream): BufferedReader
}

class InputStreamSerializationRepoHelper@Inject constructor() : IInputStreamSerializationRepoHelper {
    override fun getBufferReader(inputStream: InputStream): BufferedReader {
        return inputStream.bufferedReader()
    }
}