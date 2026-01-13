package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.DataConstants.Companion.JSON_EXTENSION
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplateStatus
import com.jonpeps.gamescms.data.helpers.toCommonFilename
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateDetailsListRepository
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplatesViewModelData
import com.jonpeps.gamescms.ui.tabletemplates.serialization.UpdatedTableTemplatesViewModelData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ISerializeTableTemplatesViewModel {
    fun readItemsFromAssets(assetPath: String, directory: String, fileName: String)
    fun readItems(directory: String, fileName: String)
    fun updateTableTemplate(templateName: String,
                            templatesListPath: String,
                            item: TableTemplateItemListMoshi,
                            details: TableTemplateDetailsListMoshi,
                            success: Boolean)
}

@HiltViewModel
class SerializeTableTemplatesViewModel
@Inject constructor(private val coroutineDispatcher: CoroutineDispatcher,
                    private val inputStreamTableTemplateStatus: InputStreamTableTemplateStatus,
                    private val moshiTableTemplateRepository: MoshiTableTemplateRepository,
                    private val moshiTableTemplateDetailsListRepository: MoshiTableTemplateDetailsListRepository,
                    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper
)
    : ViewModel(), ISerializeTableTemplatesViewModel {
        private val _serializeStatus = MutableStateFlow(SerializeTableTemplatesViewModelData(false, null, ""))
    val serializeStatus: StateFlow<SerializeTableTemplatesViewModelData> = _serializeStatus

    private val _updatedTableTemplateStatus = MutableStateFlow(
        UpdatedTableTemplatesViewModelData(
            false,
            ""
        )
    )

    val updatedTableTemplateStatus: StateFlow<UpdatedTableTemplatesViewModelData> = _updatedTableTemplateStatus

    override fun readItemsFromAssets(assetPath: String, directory: String, fileName: String) {
        viewModelScope.launch(coroutineDispatcher) {
            val inputStream = commonSerializationRepoHelper.getInputStreamFromStr(assetPath)
            inputStream?.let {
                inputStreamTableTemplateStatus.processSuspend(it, directory, fileName)
                _serializeStatus.value = SerializeTableTemplatesViewModelData(
                    inputStreamTableTemplateStatus.status.success,
                    inputStreamTableTemplateStatus.status.item,
                    inputStreamTableTemplateStatus.status.errorMessage)
            }?:run {
                _serializeStatus.value = SerializeTableTemplatesViewModelData(false, null,
                    FAILED_TO_FIND_ASSET_FILE)
            }
        }
    }

    override fun readItems(directory: String, fileName: String) {
        viewModelScope.launch(coroutineDispatcher) {
            moshiTableTemplateRepository.setAbsoluteFile(commonSerializationRepoHelper.getAbsoluteFile(directory,
                fileName + JSON_EXTENSION))
            if (moshiTableTemplateDetailsListRepository.load()) {
                _serializeStatus.value = SerializeTableTemplatesViewModelData(true,
                    moshiTableTemplateDetailsListRepository.getItem(), "")
            } else {
                _serializeStatus.value = SerializeTableTemplatesViewModelData(false, null,
                    moshiTableTemplateDetailsListRepository.getErrorMsg())
            }
        }
    }

    override fun updateTableTemplate(templateName: String,
                                     templatesListPath: String,
                                     item: TableTemplateItemListMoshi,
                                     details: TableTemplateDetailsListMoshi,
                                     success: Boolean
    ) {
        viewModelScope.launch(coroutineDispatcher) {
            val filename = templateName.toCommonFilename() + JSON_EXTENSION
            val file = commonSerializationRepoHelper.getAbsoluteFile(templatesListPath,
                filename)
            moshiTableTemplateRepository.setAbsoluteFile(file)
            if (moshiTableTemplateRepository.save(item)) {
                var found = false
                for (tableTemplateItem in details.items) {
                    if (tableTemplateItem.name == templateName) {
                        tableTemplateItem.status = success
                        found = true
                        break
                    }
                }
                if (!found) {
                    details.items.add(TableTemplateDetailsMoshi(templateName, filename, success))
                }
                if (moshiTableTemplateDetailsListRepository.save(details)) {
                    _updatedTableTemplateStatus.value = UpdatedTableTemplatesViewModelData(true, "")
                } else {
                    _updatedTableTemplateStatus.value = UpdatedTableTemplatesViewModelData(false,
                        moshiTableTemplateDetailsListRepository.getErrorMsg())
                }
            } else {
                _updatedTableTemplateStatus.value = UpdatedTableTemplatesViewModelData(false,
                    moshiTableTemplateRepository.getErrorMsg())
            }
        }
    }

    companion object {
        const val FAILED_TO_FIND_ASSET_FILE = "Failed to load asset file!"
    }
}