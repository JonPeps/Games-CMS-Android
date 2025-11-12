package com.jonpeps.gamescms.data.viewmodels

import com.jonpeps.gamescms.data.DataConstants.Companion.JSON_EXTENSION
import com.jonpeps.gamescms.data.repositories.IBaseSingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import java.io.IOException
import java.io.InputStream

data class InputStreamToJsonStorageStatus<T>(
    val success: Boolean,
    val item: T?,
    val errorMessage: String,
    val exception: Exception?
)

interface IInputStreamToJsonTypeToStorage {
    suspend fun processSuspend(inputStream: InputStream, directory: String, fileName: String)
}

open class InputStreamToJsonTypeToStorage<T>(
    private val singleItemMoshiJsonRepository: IBaseSingleItemMoshiJsonRepository<T>,
    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    private val inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
): IInputStreamToJsonTypeToStorage {
    lateinit var status: InputStreamToJsonStorageStatus<T>

    var exception: Exception? = null
    private var item: T? = null

    override suspend fun processSuspend(inputStream: InputStream, directory: String, fileName: String) {
        exception = null
        var errorMessage = ""
        var success = true
        try {
            initReadFiles(inputStream)
            if (singleItemMoshiJsonRepository.serialize(commonSerializationRepoHelper.readAll(inputStream))) {
                item = singleItemMoshiJsonRepository.getItem()
                if (item == null) {
                    success = false
                    errorMessage = FAILED_TO_LOAD_FILE
                }
            } else {
                success = false
                errorMessage = FAILED_TO_LOAD_FILE
            }
        } catch (ex: Exception) {
            exception = ex
            errorMessage = ex.message.toString()
            success = false
        }
        if (success) {
            try {
                if (!commonSerializationRepoHelper.createDirectory(directory)) {
                    success = false
                    errorMessage = FAILED_TO_CREATE_DIR + directory
                } else {
                    initWriteFiles(fileName, directory)
                    if (singleItemMoshiJsonRepository.save(item!!)) {
                        success = true
                        errorMessage = ""
                    } else {
                        success = false
                        errorMessage = FAILED_TO_WRITE_FILE
                    }
                }
            } catch (ex: Exception) {
                exception = ex
                errorMessage = ex.message.toString()
                success = false
            }
        }
        status = InputStreamToJsonStorageStatus(success, item, errorMessage, exception)
    }

    private fun initReadFiles(inputStream: InputStream) {
        singleItemMoshiJsonRepository.setBufferReader(
            inputStreamSerializationRepoHelper.getBufferReader(inputStream)
        )
    }

    private fun initWriteFiles(fileName: String, directory: String) {
        val completeFilename = fileName + JSON_EXTENSION
        singleItemMoshiJsonRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(directory, completeFilename))
        singleItemMoshiJsonRepository.setFile(commonSerializationRepoHelper.getMainFile(completeFilename))
        singleItemMoshiJsonRepository.assignDirectoryFile(
            commonSerializationRepoHelper.getDirectoryFile(directory))
        singleItemMoshiJsonRepository.setFileWriter(
            commonSerializationRepoHelper.getFileWriter(directory, completeFilename))
        item?.let {
            singleItemMoshiJsonRepository.setItem(it)
        }?:run {
            throw IOException(ITEM_TO_WRITE_IS_NULL)
        }
    }

    companion object {
        const val ITEM_TO_WRITE_IS_NULL = "Item to write to storage is null"
        const val FAILED_TO_LOAD_FILE = "Failed to load string list from assets!"
        const val FAILED_TO_WRITE_FILE = "Failed to write string list to local storage!"
        const val FAILED_TO_CREATE_DIR = "Failed to create directory: "
    }
}