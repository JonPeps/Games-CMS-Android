package com.jonpeps.gamescms.ui.tabletemplates.serialization

import android.content.Context
import android.content.res.AssetManager
import com.jonpeps.gamescms.data.DataConstants.Companion.FILE_EXTENSION
import com.jonpeps.gamescms.data.DataConstants.Companion.JSON_EXTENSION
import com.jonpeps.gamescms.data.dataclasses.TableTemplateStatus
import com.jonpeps.gamescms.data.dataclasses.TableTemplateStatusList
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplate
import com.jonpeps.gamescms.data.helpers.StringListToSplitItemList
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateRepository
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateStatusListRepository
import com.jonpeps.gamescms.data.serialization.CommonSerializationRepoHelper
import java.io.File
import javax.inject.Inject

interface ISerializeTableTemplates {
    suspend fun serializeFromAssets(filename: String)
    suspend fun readItems(filename: String)
    suspend fun updateTableTemplate(templateName: String, item: TableTemplateItemListMoshi, templatesListFilename: String)

    var serializeTableTemplatesStatus: SerializeTableTemplatesStatus
    var serializeUpdateTableTemplateStatus: SerializeUpdateTableTemplateStatus
}

class SerializeTableTemplates@Inject constructor(
    private val context: Context,
    private val assetManager: AssetManager,
    private val stringListToSplitItemList: StringListToSplitItemList,
    private val inputStreamTableTemplate: InputStreamTableTemplate,
    private val moshiTableTemplateRepository: MoshiTableTemplateRepository,
    private val moshiTableTemplateStatusListRepository: MoshiTableTemplateStatusListRepository,
    private val commonSerializationRepoHelper: CommonSerializationRepoHelper,
    private val serializeTableTemplateHelpers: SerializeTableTemplateHelpers,
    private val serializeTableTemplateUpdateCore: SerializeTableTemplateUpdateCore
) : ISerializeTableTemplates {
    override var serializeTableTemplatesStatus: SerializeTableTemplatesStatus =
        SerializeTableTemplatesStatus(false, null,"")
    override var serializeUpdateTableTemplateStatus: SerializeUpdateTableTemplateStatus =
        SerializeUpdateTableTemplateStatus(false, "")
    private var success = true
    private var errorMessage = ""

    override suspend fun serializeFromAssets(filename: String) {
        success = true
        val statusList: ArrayList<TableTemplateStatus> = arrayListOf()
        try {
            var path = TEMPLATES_FOLDER + filename
            var inputStream = assetManager.open(path)
            stringListToSplitItemList.loadSuspend(inputStream)
            if (stringListToSplitItemList.status.success) {
                val filenames = stringListToSplitItemList.status.fileNames
                val names = stringListToSplitItemList.status.names
                val externalPath = context.getExternalFilesDir(null)
                externalPath?.let {
                    val absolutePath = it.absolutePath + TEMPLATES_FOLDER + "/"
                    var counter = 0
                    for (filename in filenames) {
                        path = absolutePath + filename
                        inputStream = assetManager.open(path)
                        inputStreamTableTemplate.processSuspend(
                            inputStream,
                            absolutePath,
                            filename
                        )
                        statusList.add(
                            TableTemplateStatus(
                                inputStreamTableTemplate.status.success,
                                names[counter],
                                filename,
                                inputStreamTableTemplate.status.errorMessage
                            )
                        )
                        counter++
                    }
                    if (!moshiTableTemplateStatusListRepository.save(TableTemplateStatusList(statusList))) {
                        setError(FAILED_TO_SAVE_TEMPLATES_STATUS)
                    }
                }?: run {
                    setError(EXTERNAL_STORAGE_PATH_IS_NULL)
                }
            } else {
                setError(stringListToSplitItemList.status.errorMessage)
            }
        } catch (ex: Exception) {
            success = false
            errorMessage = EXCEPTION_THROWN_MSG + ex.message
        }
        serializeTableTemplatesStatus = SerializeTableTemplatesStatus(success, statusList, errorMessage)
    }

    override suspend fun readItems(filename: String) {
        success = true
        val statusList: ArrayList<TableTemplateStatus> = arrayListOf()
        try {
            val externalPath = context.getExternalFilesDir(null)
            externalPath?.let {
                val absolutePath = it.absolutePath + TEMPLATES_FOLDER + "/" + filename + JSON_EXTENSION
                val file = File(absolutePath)
                val inputStream = commonSerializationRepoHelper.getInputStream(file)
                inputStream?.let { item ->
                    stringListToSplitItemList.loadSuspend(item)
                    if (stringListToSplitItemList.status.success) {
                        val filenames = stringListToSplitItemList.status.fileNames
                        val names = stringListToSplitItemList.status.names
                        for (name in names) {
                            statusList.add(
                                TableTemplateStatus(
                                    true,
                                    name,
                                    filenames[filename.indexOf(name)],
                                    null
                                )
                            )
                        }
                    } else {
                        setError(stringListToSplitItemList.status.errorMessage)
                    }
                } ?: run {
                    setError(FILE_DOES_NOT_EXIST + filename)
                }
            }?: run {
                setError(EXTERNAL_STORAGE_PATH_IS_NULL)
            }
        } catch (ex: Exception) {
            setError(EXCEPTION_THROWN_MSG + ex.message)
        }
        serializeTableTemplatesStatus = SerializeTableTemplatesStatus(success, statusList, errorMessage)
    }

    override suspend fun updateTableTemplate(
        templateName: String,
        item: TableTemplateItemListMoshi,
        templatesListFilename: String
    ) {
        success = true
        val externalPath = context.getExternalFilesDir(null)
        var externalPathValid = ""
        try {
            externalPath?.let {
                externalPathValid = it.absolutePath
                val absolutePath = it.absolutePath + TEMPLATES_FOLDER + "/" + templatesListFilename + JSON_EXTENSION
                val file = File(absolutePath)
                val inputStream = commonSerializationRepoHelper.getInputStream(file)
                inputStream?.let { inputStreamItem ->
                    stringListToSplitItemList.loadSuspend(inputStreamItem)
                    if (stringListToSplitItemList.status.success) {
                        serializeTableTemplateUpdateCore.update(
                            templatesListFilename,
                            absolutePath,
                            templateName,
                            stringListToSplitItemList.status.names,
                            stringListToSplitItemList.status.fileNames
                        )
                        if (!serializeTableTemplateUpdateCore.status.success) {
                            setError(serializeTableTemplateUpdateCore.status.errorMessage)
                        }
                    } else {
                        setError(stringListToSplitItemList.status.errorMessage)
                    }
                }?: run {
                    setError(FILE_DOES_NOT_EXIST + templatesListFilename)
                }
            }?: run {
                setError(EXTERNAL_STORAGE_PATH_IS_NULL)
            }
            if (success) {
                val absolutePath = "$externalPathValid$TEMPLATES_FOLDER/"
                initTableTemplateWriteFiles(serializeTableTemplateUpdateCore.status.templateFilename,
                    absolutePath, item)
                if (!moshiTableTemplateRepository.save(item)) {
                    setError(FAILED_TO_SAVE_TEMPLATE)
                }
            }
        } catch (ex: Exception) {
            setError(EXCEPTION_THROWN_MSG + ex.message + ex.message)
        }
        serializeUpdateTableTemplateStatus = SerializeUpdateTableTemplateStatus(success, errorMessage)
    }

    private fun setError(msg: String) {
        success = false
        errorMessage = msg
    }

    private fun initTableTemplateWriteFiles(filename: String, directory: String, tableTemplates: TableTemplateItemListMoshi) {
        moshiTableTemplateRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(directory, filename))
        moshiTableTemplateRepository.setFile(commonSerializationRepoHelper.getMainFile(filename))
        moshiTableTemplateRepository.assignDirectoryFile(
            commonSerializationRepoHelper.getDirectoryFile(directory))
        moshiTableTemplateRepository.setFileWriter(
            commonSerializationRepoHelper.getFileWriter(directory, filename))
        moshiTableTemplateRepository.setItem(tableTemplates)
    }

    companion object {
        const val EXCEPTION_THROWN_MSG = "Exception thrown: "
        const val EXTERNAL_STORAGE_PATH_IS_NULL = "External storage path is null!"
        const val FILE_DOES_NOT_EXIST = "File does not exist: "
        const val STRING_LIST_ITEM_IS_NULL = "StringListMoshi item is null!"
        const val TEMPLATES_FOLDER = "templates/"
        const val FAILED_TO_SAVE_TEMPLATES_STATUS = "Failed to save table templates status!"
        const val FAILED_TO_SAVE_TEMPLATE = "Failed to save table template!"
    }
}