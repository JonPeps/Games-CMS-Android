package com.jonpeps.gamescms.ui.viewmodels.defaults

import android.content.Context
import android.content.res.AssetManager
import com.jonpeps.gamescms.data.dataclasses.TableTemplateStatus
import com.jonpeps.gamescms.data.dataclasses.TableTemplateStatusList
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplate
import com.jonpeps.gamescms.data.helpers.StringListToSplitItemList
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateStatusListRepository
import com.jonpeps.gamescms.data.serialization.CommonSerializationRepoHelper
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

data class SerializeDefaultTemplatesStatus(
    val success: Boolean,
    val errorMessage: String?
)

interface ISerializeDefaultTemplates {
    suspend fun serializeFromAssets(tableTemplatesListFilename: String)
    suspend fun serializeFromFile(mainFile: File)
}

class SerializeDefaultTemplates@Inject constructor(
    private val context: Context,
    private val assetManager: AssetManager,
    private val stringListToSplitItemList: StringListToSplitItemList,
    private val inputStreamTableTemplate: InputStreamTableTemplate,
    private val moshiTableTemplateStatusListRepository: MoshiTableTemplateStatusListRepository,
    private val commonSerializationRepoHelper: CommonSerializationRepoHelper
) : ISerializeDefaultTemplates {
    var serializeDefaultTemplatesStatus: SerializeDefaultTemplatesStatus =
        SerializeDefaultTemplatesStatus(false, "")

    override suspend fun serializeFromAssets(tableTemplatesListFilename: String) {
        var success = true
        var errorMessage = ""
        try {
            var path = DEFAULT_TEMPLATES_FOLDER + tableTemplatesListFilename
            var inputStream = assetManager.open(path)
            stringListToSplitItemList.loadSuspend(inputStream)
            if (stringListToSplitItemList.status.success) {
                val statusList: ArrayList<TableTemplateStatus> = arrayListOf()
                val filenames = stringListToSplitItemList.status.fileNames
                val names = stringListToSplitItemList.status.names
                val externalPath = context.getExternalFilesDir(null)
                val absolutePath = externalPath?.absolutePath + DEFAULT_TEMPLATES_FOLDER + "/"
                for (filename in filenames) {
                    path = absolutePath + filename
                    inputStream = assetManager.open(path)
                    inputStreamTableTemplate.processSuspend(inputStream,
                        absolutePath,
                        filename)
                    statusList.add(TableTemplateStatus(inputStreamTableTemplate.status.success,
                        names[filenames.indexOf(filename)],
                        inputStreamTableTemplate.status.errorMessage))
                }
                if (!moshiTableTemplateStatusListRepository.save(TableTemplateStatusList(statusList))) {
                    success = false
                    errorMessage = FAILED_TO_SAVE_TEMPLATES_STATUS
                }
            } else {
                success = false
                errorMessage = stringListToSplitItemList.status.errorMessage?:""
            }
        } catch (ex: Exception) {
            success = false
            errorMessage = ex.message?:""
        }
        serializeDefaultTemplatesStatus = SerializeDefaultTemplatesStatus(success,
            errorMessage)
    }

    override suspend fun serializeFromFile(mainFile: File) {
        var success = true
        var errorMessage = ""
        if (mainFile.exists()) {
            try {
                stringListToSplitItemList.loadSuspend(mainFile.inputStream())
                if (stringListToSplitItemList.status.success) {
                    val statusList: ArrayList<TableTemplateStatus> = arrayListOf()
                    val filenames = stringListToSplitItemList.status.fileNames
                    val names = stringListToSplitItemList.status.names
                    val externalPath = context.getExternalFilesDir(null)
                    val absolutePath = externalPath?.absolutePath + DEFAULT_TEMPLATES_FOLDER + "/"
                    for (filename in filenames) {
                        val mainFile = File(absolutePath + filename)
                        val inputStream: FileInputStream? = commonSerializationRepoHelper.getInputStream(mainFile)
                        inputStream?.let {
                            inputStreamTableTemplate.processSuspend(
                                it,
                                absolutePath,
                                filename
                            )
                        }?:run {
                            success = false
                            errorMessage = FILE_DOES_NOT_EXIST + mainFile.absoluteFile
                        }
                        statusList.add(
                            TableTemplateStatus(
                                success,
                                names[filenames.indexOf(filename)],
                                errorMessage
                            )
                        )
                    }
                    if (!moshiTableTemplateStatusListRepository
                        .save(TableTemplateStatusList(statusList))) {
                        success = false
                        errorMessage = FAILED_TO_SAVE_TEMPLATES_STATUS
                    }
                } else {
                    success = false
                    errorMessage = stringListToSplitItemList.status.errorMessage ?: ""
                }
            } catch (ex: Exception) {
                success = false
                errorMessage = ex.message ?: ""
            }
        } else {
            success = false
            errorMessage = FILE_DOES_NOT_EXIST + mainFile.absoluteFile.path
        }
        serializeDefaultTemplatesStatus = SerializeDefaultTemplatesStatus(success,
            errorMessage)
    }

    companion object {
        const val FILE_DOES_NOT_EXIST = "File does not exist: "
        const val DEFAULT_TEMPLATES_FOLDER = "default_templates/"
        const val FAILED_TO_SAVE_TEMPLATES_STATUS = "Failed to save templates status!"
    }
}