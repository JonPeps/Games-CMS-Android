package com.jonpeps.gamescms.data.serialization

import java.io.File

enum class SubDeleteFlag {
    NONE,
    FILE,
    DIRECTORY_AND_FILES
}

interface ICommonDeleteFileHelper {
    fun deleteFile(path: String, name: String): Boolean
    fun deleteDirectory(directory: String)
    fun onSubDelete(path: String, name: String, subDeleteFlag: SubDeleteFlag): Boolean
}

class CommonDeleteFileHelper : ICommonDeleteFileHelper {
    override fun deleteFile(path: String, name: String): Boolean {
        return try {
            File(path + name).delete()
        } catch (ex: Exception) {
            false
        }
    }

    override fun deleteDirectory(directory: String) {
        val fileOrDir = File(directory)
        if (fileOrDir.exists() && fileOrDir.isDirectory) {
            fileOrDir.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    deleteDirectory(directory)
                } else {
                    file.delete()
                }
            }
            fileOrDir.delete()
        } else {
            fileOrDir.delete()
        }
    }

    override fun onSubDelete(path: String, name: String, subDeleteFlag: SubDeleteFlag): Boolean {
        return when (subDeleteFlag) {
            SubDeleteFlag.NONE -> true
            SubDeleteFlag.FILE -> {
                deleteFile(path, name)
            }
            SubDeleteFlag.DIRECTORY_AND_FILES -> {
                val file = File(path)
                return if (file.exists()) {
                    deleteDirectory(path + name)
                    true
                } else {
                    false
                }
            }
        }
    }
}