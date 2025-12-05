package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateStatusListRepository
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplatesViewModelData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ISerializeTableTemplatesViewModel {
    fun serializeFromAssets(filename: String)
    fun readItems(filename: String)
    fun updateTableTemplate(templateName: String, item: TableTemplateItemListMoshi, templatesListFilename: String)
}

@HiltViewModel
class SerializeTableTemplatesViewModel
@Inject constructor(private val coroutineDispatcher: CoroutineDispatcher,
                    private val serializeTableTemplates: SerializeTableTemplates,
                    private val moshiTableTemplateStatusListRepository: MoshiTableTemplateStatusListRepository
)
    : ViewModel(), ISerializeTableTemplatesViewModel {
        private val _status = MutableStateFlow(SerializeTableTemplatesViewModelData(false, ""))
    val status: StateFlow<SerializeTableTemplatesViewModelData> = _status

    override fun serializeFromAssets(filename: String) {
        viewModelScope.launch(coroutineDispatcher) {
            serializeTableTemplates.serializeFromAssets(filename)
            val status = serializeTableTemplates.serializeTableTemplatesStatus
            var success = false
            var errorMessage = ""
            if (status.success) {
                if (moshiTableTemplateStatusListRepository.save(status)) {
                    success = true
                } else {
                    errorMessage = moshiTableTemplateStatusListRepository.getErrorMsg()
                }
            } else {
                errorMessage = status.errorMessage
            }
            _status.value = SerializeTableTemplatesViewModelData(success, errorMessage)
        }
    }

    override fun readItems(filename: String) {
        viewModelScope.launch(coroutineDispatcher) {
            serializeTableTemplates.readItems(filename)
        }
    }

    override fun updateTableTemplate(
        templateName: String,
        item: TableTemplateItemListMoshi,
        templatesListFilename: String
    ) {
        TODO("Not yet implemented")
    }

}