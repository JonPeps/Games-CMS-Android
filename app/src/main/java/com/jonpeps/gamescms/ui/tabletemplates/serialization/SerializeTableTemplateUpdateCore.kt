package com.jonpeps.gamescms.ui.tabletemplates.serialization

import com.jonpeps.gamescms.data.DataConstants.Companion.JSON_EXTENSION
import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.CommonSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates.Companion.STRING_LIST_ITEM_IS_NULL
import java.io.File
import javax.inject.Inject

interface ISerializeTableTemplateUpdateCore {
    suspend fun update(templatesListFilename: String,
                       absolutePath: String,
                       templateName: String,
                       names: List<String>,
                       fileNames: List<String>)

    val status: SerializeTableTemplateUpdateCoreStatus
}

class SerializeTableTemplateUpdateCore@Inject constructor(
    private val serializeTableTemplateHelpers: SerializeTableTemplateHelpers,
    private val stringListRepository: MoshiStringListRepository,
    private val commonSerializationRepoHelper: CommonSerializationRepoHelper)
    : ISerializeTableTemplateUpdateCore{

    override lateinit var status: SerializeTableTemplateUpdateCoreStatus
    private var success = true
    private var errorMessage = ""

    override suspend fun update(templatesListFilename: String,
                                absolutePath: String,
                                templateName: String,
                                names: List<String>,
                                fileNames: List<String>) {
        var overwriteFilename = false
        var filename = ""
        val namesArrayList = ArrayList(names)
        val fileNamesArrayList = ArrayList(fileNames)
        initFiles(templatesListFilename, absolutePath)
        try {
            val finalPath = absolutePath + templatesListFilename + JSON_EXTENSION
            val file = File(finalPath)
            val inputStream = commonSerializationRepoHelper.getInputStream(file)
            inputStream?.let { item ->
                if (stringListRepository.load()) {
                    val stringList = stringListRepository.getItem()
                    stringList?.let { item ->
                        val itemsArrayList = ArrayList(item.items)
                        val index = names.indexOf(templateName)
                        if (index >= 0) {
                            itemsArrayList.removeAt(index)
                            overwriteFilename = true
                        }
                        filename = serializeTableTemplateHelpers.getFilename(templateName)
                        itemsArrayList.add("$templateName:${filename}")
                        namesArrayList.add(templateName)
                        fileNamesArrayList.add(filename)
                        if (!stringListRepository.save(StringListMoshi(itemsArrayList))) {
                            setError(FAILED_TO_SAVE_TEMPLATE_LIST)
                        }
                    }?: run {
                        setError(STRING_LIST_ITEM_IS_NULL)
                    }
                } else {
                    setError(stringListRepository.getErrorMsg())
                }
            }?: run {
                setError(FILE_DOES_NOT_EXIST + finalPath)
            }
        } catch (ex: Exception) {
            setError(EXCEPTION_THROWN_MSG + ex.message)
        }
        status = SerializeTableTemplateUpdateCoreStatus(success,
            overwriteFilename,
            namesArrayList, fileNamesArrayList, filename,
            errorMessage)
    }

    private fun setError(msg: String) {
        success = false
        errorMessage = msg
    }

    private fun initFiles(filename: String, directory: String) {
        stringListRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(directory, filename))
        stringListRepository.setFile(commonSerializationRepoHelper.getMainFile(filename))
        stringListRepository.assignDirectoryFile(
            commonSerializationRepoHelper.getDirectoryFile(directory))
        stringListRepository.setFileWriter(
            commonSerializationRepoHelper.getFileWriter(directory, filename))
        commonSerializationRepoHelper.getBufferReader(directory, filename)
        stringListRepository.setFile(commonSerializationRepoHelper.getMainFile(filename))
        stringListRepository.assignDirectoryFile(
            commonSerializationRepoHelper.getDirectoryFile(directory))
        stringListRepository.setFileWriter(
            commonSerializationRepoHelper.getFileWriter(directory, filename))
    }

    companion object {
        const val FAILED_TO_SAVE_TEMPLATE_LIST = "Failed to save table template list!"
        const val EXCEPTION_THROWN_MSG = "Exception thrown: "
        const val FILE_DOES_NOT_EXIST = "File does not exist: "
    }
}