package com.jonpeps.gamescms.ui.viewmodels

import android.content.Context
import android.content.res.AssetManager
import com.jonpeps.gamescms.data.dataclasses.TableTemplateStatus
import com.jonpeps.gamescms.data.dataclasses.TableTemplateStatusList
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplate
import com.jonpeps.gamescms.data.helpers.StringListToSplitItemList
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateStatusListRepository
import com.jonpeps.gamescms.data.serialization.CommonSerializationRepoHelper
import java.io.File
import javax.inject.Inject

data class SerializeTableTemplatesStatus(
    val success: Boolean,
    val items: List<TableTemplateStatus>?,
    val errorMessage: String = ""
)

interface ISerializeTableTemplates {
    suspend fun serializeFromAssets(filename: String)
    suspend fun serializeItems(filename: String)
    suspend fun addTableTemplate(name: String, item: TableTemplateItemListMoshi)
}

class SerializeTableTemplates@Inject constructor(
    private val context: Context,
    private val assetManager: AssetManager,
    private val stringListToSplitItemList: StringListToSplitItemList,
    private val inputStreamTableTemplate: InputStreamTableTemplate,
    private val moshiTableTemplateStatusListRepository: MoshiTableTemplateStatusListRepository,
    private val commonSerializationRepoHelper: CommonSerializationRepoHelper
) : ISerializeTableTemplates {
    var serializeTableTemplatesStatus: SerializeTableTemplatesStatus =
        SerializeTableTemplatesStatus(false, null,"")

    override suspend fun serializeFromAssets(filename: String) {
        var success = true
        var errorMessage = ""
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
                        success = false
                        errorMessage = FAILED_TO_SAVE_TEMPLATES_STATUS
                    }
                }?: run {
                    success = false
                    errorMessage = EXTERNAL_STORAGE_PATH_IS_NULL
                }
            } else {
                success = false
                errorMessage = stringListToSplitItemList.status.errorMessage
            }
        } catch (ex: Exception) {
            success = false
            errorMessage = EXCEPTION_THROWN_MSG + ex.message
        }
        serializeTableTemplatesStatus = SerializeTableTemplatesStatus(success, statusList, errorMessage)
    }

    override suspend fun serializeItems(filename: String) {
        var success = true
        var errorMessage = ""
        val statusList: ArrayList<TableTemplateStatus> = arrayListOf()
        try {
            val file = File(filename)
            val inputStream = commonSerializationRepoHelper.getInputStream(file)
            inputStream?.let {
                stringListToSplitItemList.loadSuspend(it)
                if (stringListToSplitItemList.status.success) {
                    val filenames = stringListToSplitItemList.status.fileNames
                    val names = stringListToSplitItemList.status.names
                    for (name in names) {
                        statusList.add(TableTemplateStatus(true,
                            name,
                            filenames[filename.indexOf(name)],
                            null))
                    }
                } else {
                    success = false
                    errorMessage = stringListToSplitItemList.status.errorMessage
                }
            }?: run {
                success = false
                errorMessage = FILE_DOES_NOT_EXIST + filename
            }
        } catch (ex: Exception) {
            success = false
            errorMessage = EXCEPTION_THROWN_MSG + ex.message
        }
        serializeTableTemplatesStatus = SerializeTableTemplatesStatus(success, statusList, errorMessage)
    }

    override suspend fun addTableTemplate(
        name: String,
        item: TableTemplateItemListMoshi
    ) {

    }

    companion object {
        const val EXCEPTION_THROWN_MSG = "Exception thrown: "
        const val EXTERNAL_STORAGE_PATH_IS_NULL = "External storage path is null!"
        const val FILE_DOES_NOT_EXIST = "File does not exist: "
        const val TEMPLATES_FOLDER = "templates/"
        const val FAILED_TO_SAVE_TEMPLATES_STATUS = "Failed to save templates status!"
    }
}