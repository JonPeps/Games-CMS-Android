package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

interface ITableTemplateRepository {
    suspend fun loadTemplate(): Boolean
    suspend fun saveTemplate(template: TableItemList): Boolean
    suspend fun deleteTemplate(): Boolean
    fun setFilePath(path: File)
    fun setFile(file: File)
    fun setAbsolutePath(file: File)
    fun setBufferReader(bufferedReader: BufferedReader)
    fun setFileWriter(fileWriter: FileWriter)
    fun getItem(): TableItemList?
    fun getErrorMsg(): String
}

class TableTemplateRepository
@Inject constructor(private val stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
                    private val tableItemListMoshiSerialization: ITableItemListMoshiSerialization
)
    : ITableTemplateRepository {

    private lateinit var filePath: File
    private lateinit var file: File
    private lateinit var absolutePath: File
    private lateinit var bufferedReader: BufferedReader
    private lateinit var fileWriter: FileWriter
    private var tableItemList: TableItemList? = null
    private var errorMsg = ""

    override suspend fun loadTemplate(): Boolean {
        errorMsg = ""
        var success = true
        if (stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)) {
            if(tableItemListMoshiSerialization.fromJson(stringFileStorageStrSerialisation.getContents())) {
                tableItemList = tableItemListMoshiSerialization.getItem()
            } else {
                errorMsg = tableItemListMoshiSerialization.getErrorMsg()
                success = false
            }
        } else {
            errorMsg = stringFileStorageStrSerialisation.getErrorMsg()
            success = false
        }
        return success
    }

    override suspend fun saveTemplate(template: TableItemList): Boolean {
        errorMsg = ""
        errorMsg = if (tableItemListMoshiSerialization.toJson(template)) {
            if (stringFileStorageStrSerialisation
                .write(filePath, file, fileWriter, tableItemListMoshiSerialization.getToJsonItem())) {
                return true
            } else {
                stringFileStorageStrSerialisation.getErrorMsg()
            }
        } else {
            tableItemListMoshiSerialization.getErrorMsg()
        }
        return false
    }

    override suspend fun deleteTemplate(): Boolean {
        errorMsg = ""
        if (absolutePath.delete()) {
            return true
        } else {
            errorMsg = FAILED_TO_DELETE_FILE + absolutePath
            return false
        }
    }

    override fun setFilePath(path: File) {
        filePath = path
    }
    override fun setFile(file: File) {
        this.file = file
    }

    override fun setAbsolutePath(file: File) {
        absolutePath = file
    }

    override fun setBufferReader(bufferedReader: BufferedReader) {
        this.bufferedReader = bufferedReader
    }

    override fun setFileWriter(fileWriter: FileWriter) {
        this.fileWriter = fileWriter
    }

    override fun getItem(): TableItemList? = tableItemList
    override fun getErrorMsg(): String = errorMsg

    companion object {
        const val FAILED_TO_DELETE_FILE = "Failed to delete file: "
    }
}