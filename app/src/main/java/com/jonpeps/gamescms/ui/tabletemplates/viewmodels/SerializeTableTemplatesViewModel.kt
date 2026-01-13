package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplateStatus
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplatesViewModelData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ISerializeTableTemplatesViewModel {
    fun serializeFromAssets(assetPath: String, directory: String, fileName: String)
    fun readItems(directory: String, fileName: String)
    fun updateTableTemplate(templateName: String, item: TableTemplateItemListMoshi, templatesListFilename: String)
}

@HiltViewModel
class SerializeTableTemplatesViewModel
@Inject constructor(private val coroutineDispatcher: CoroutineDispatcher,
                    private val inputStreamTableTemplateStatus: InputStreamTableTemplateStatus,
                    private val moshiTableTemplateRepository: MoshiTableTemplateRepository,
                    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper
)
    : ViewModel(), ISerializeTableTemplatesViewModel {
        private val _status = MutableStateFlow(SerializeTableTemplatesViewModelData(false, null, ""))
    val status: StateFlow<SerializeTableTemplatesViewModelData> = _status

    override fun serializeFromAssets(assetPath: String, directory: String, fileName: String) {
        viewModelScope.launch(coroutineDispatcher) {
            val inputStream = commonSerializationRepoHelper.getInputStreamFromStr(assetPath)
            inputStream?.let {
                inputStreamTableTemplateStatus.processSuspend(it, directory, fileName)
                _status.value = SerializeTableTemplatesViewModelData(
                    inputStreamTableTemplateStatus.status.success,
                    inputStreamTableTemplateStatus.status.item,
                    inputStreamTableTemplateStatus.status.errorMessage)
            }?:run {
                _status.value = SerializeTableTemplatesViewModelData(false, null,
                    FAILED_TO_ASSET_FILE)
            }
        }
    }

    override fun readItems(directory: String, fileName: String) {
        viewModelScope.launch(coroutineDispatcher) {
            moshiTableTemplateRepository.setAbsoluteFile(commonSerializationRepoHelper.getAbsoluteFile(directory, fileName))
            if (moshiTableTemplateRepository.load()) {
                _status.value = SerializeTableTemplatesViewModelData(true, moshiTableTemplateRepository.getItem(), "")
            } else {
                _status.value = SerializeTableTemplatesViewModelData(false, null, moshiTableTemplateRepository.getErrorMsg())
            }
        }
    }

    override fun updateTableTemplate(
        templateName: String,
        item: TableTemplateItemListMoshi,
        templatesListFilename: String
    ) {
        TODO("Not yet implemented")
    }

    companion object {
        const val FAILED_TO_ASSET_FILE = "Failed to load asset file!"
    }
}