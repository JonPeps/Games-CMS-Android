package com.jonpeps.gamescms.data.serialization

import java.io.File

interface ICommonDeleteFileHelper {
    fun deleteFile(path: String, name: String): Boolean
}

class CommonDeleteFileHelper : ICommonDeleteFileHelper {
    override fun deleteFile(path: String, name: String): Boolean {
        return try {
            File(path + name).delete()
        } catch (ex: Exception) {
            false
        }
    }
}