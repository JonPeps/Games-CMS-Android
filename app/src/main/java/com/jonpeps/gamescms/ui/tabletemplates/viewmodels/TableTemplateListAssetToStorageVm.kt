package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.helpers.JsonStringListWithFilenameHelper
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
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
    private val moshiStringListRepository: IMoshiStringListRepository,
    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    private val inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel(), ITableTemplateListAssetToStorageVm {
    private val _status =
        MutableStateFlow(TableTemplateListAssetToStorageStatus(true, null, null, "", null))
    val status: StateFlow<TableTemplateListAssetToStorageStatus> = _status

    override fun load(directory: String,
                      fileName: String,
                      inputStream: InputStream) {
        viewModelScope.launch(coroutineDispatcher) {
            val viewModel =
                InputStreamStringListViewModel(
                    inputStream,
                    directory,
                    fileName,
                    moshiStringListRepository,
                    commonSerializationRepoHelper,
                    inputStreamSerializationRepoHelper,
                    coroutineDispatcher
                )

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
                }
            } else {
                errorMessage = viewModel.status.value.errorMessage
                exception = viewModel.status.value.exception
                success = false
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
    }
}