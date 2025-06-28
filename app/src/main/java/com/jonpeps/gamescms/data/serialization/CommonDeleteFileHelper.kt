package com.jonpeps.gamescms.data.serialization

import java.io.File

enum class SubDeleteFlag {
    NONE,
    FILE,
    DIRECTORY_AND_FILES
}

interface ICommonDeleteFileHelper {
    fun deleteFile(path: String, name: String): Boolean
    fun deleteDirectory(directory: File)
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

    override fun deleteDirectory(directory: File) {
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    deleteDirectory(file)
                } else {
                    file.delete()
                }
            }
            directory.delete()
        } else {
            directory.delete()
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
                    deleteDirectory(file)
                    true
                } else {
                    false
                }
            }
        }
    }
}