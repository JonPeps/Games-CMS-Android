package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.helpers.JsonStringListWithFilenameHelper
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

data class TableTemplateListAssetToStorageStatus(
    val success: Boolean,
    val names: List<String>?,
    val fileNames: List<String>?,
    val errorMessage: String?,
    val exception: Exception?
)

interface ITableTemplateListAssetToStorageVm {
    fun load(directory: String,
             fileName: String,
             inputStream: InputStream
    )
}

@HiltViewModel
class TableTemplateListAssetToStorageVm@Inject constructor(
    private val viewModel: InputStreamStringListViewModel,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel(), ITableTemplateListAssetToStorageVm {
    private val _status =
        MutableStateFlow(TableTemplateListAssetToStorageStatus(true, null, null, "", null))
    val status: StateFlow<TableTemplateListAssetToStorageStatus> = _status

    override fun load(directory: String,
                      fileName: String,
                      inputStream: InputStream) {
        viewModelScope.launch(coroutineDispatcher) {
            viewModel.processSuspend()

            var success = true
            var itemNames: List<String> = emptyList()
            var fileNames: List<String> = emptyList()
            var errorMessage = ""
            var exception: Exception?  = null

            if (viewModel.status.value.success) {
                viewModel.status.value.item?.let {
                    val result = JsonStringListWithFilenameHelper.splitItem(it)
                    itemNames = result.itemNames
                    fileNames = result.fileNames
                }?: run {
                    errorMessage = FAILED_TO_LOAD_FILE
                    success = false
                }
            } else {
                errorMessage = viewModel.status.value.errorMessage
                exception = viewModel.status.value.exception
                success = false
            }
            if (success) {
                if (itemNames.size != fileNames.size) {
                    errorMessage = ITEMS_NOT_EQUAL
                    success = false
                }
            }

            _status.value = TableTemplateListAssetToStorageStatus(
                success,
                itemNames,
                fileNames,
                errorMessage,
                exception)
        }
    }

    companion object {
        const val FAILED_TO_LOAD_FILE = "Failed to load string list from assets!"
        const val ITEMS_NOT_EQUAL = "Names and file names not equal!"
    }
}