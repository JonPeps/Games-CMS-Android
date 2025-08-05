package com.jonpeps.gamescms.data.serialization.debug

import java.io.BufferedReader
import java.io.InputStream

interface IAssetSerializationRepoHelper {
    fun getBufferReader(inputStream: InputStream): BufferedReader
}

class AssetSerializationRepoHelper : IAssetSerializationRepoHelper {
    override fun getBufferReader(inputStream: InputStream): BufferedReader {
        return inputStream.bufferedReader()
    }
}