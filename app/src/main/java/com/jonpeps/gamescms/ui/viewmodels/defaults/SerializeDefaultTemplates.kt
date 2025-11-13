package com.jonpeps.gamescms.ui.viewmodels.defaults

import android.content.Context
import android.content.res.AssetManager
import com.jonpeps.gamescms.data.dataclasses.TableTemplateStatus
import com.jonpeps.gamescms.data.dataclasses.TableTemplateStatusList
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplate
import com.jonpeps.gamescms.data.helpers.StringListToSplitItemList
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateStatusListRepository
import okio.IOException
import javax.inject.Inject

data class SerializeDefaultTemplatesStatus(
    val success: Boolean,
    val errorMessage: String?
)

interface ISerializeDefaultTemplates {
    suspend fun serialize(tableTemplatesListFilename: String)
}

class SerializeDefaultTemplates@Inject constructor(
    private val context: Context,
    private val assetManager: AssetManager,
    private val stringListToSplitItemList: StringListToSplitItemList,
    private val inputStreamTableTemplate: InputStreamTableTemplate,
    private val moshiTableTemplateStatusListRepository: MoshiTableTemplateStatusListRepository
) : ISerializeDefaultTemplates {
    var serializeDefaultTemplatesStatus: SerializeDefaultTemplatesStatus =
        SerializeDefaultTemplatesStatus(false, "")

    override suspend fun serialize(tableTemplatesListFilename: String) {
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
                for (filename in filenames) {
                    path = DEFAULT_TEMPLATES_FOLDER + filename
                    inputStream = assetManager.open(path)
                    val externalPath = context.getExternalFilesDir(null)
                    inputStreamTableTemplate.processSuspend(inputStream,
                        externalPath?.absolutePath + DEFAULT_TEMPLATES_FOLDER + "/",
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
        } catch (ex: IOException) {
            success = false
            errorMessage = ex.message?:""
        }
        serializeDefaultTemplatesStatus = SerializeDefaultTemplatesStatus(success,
            errorMessage)
    }

    companion object {
        const val DEFAULT_TEMPLATES_FOLDER = "default_templates/"
        const val FAILED_TO_SAVE_TEMPLATES_STATUS = "Failed to save templates status!"
    }
}