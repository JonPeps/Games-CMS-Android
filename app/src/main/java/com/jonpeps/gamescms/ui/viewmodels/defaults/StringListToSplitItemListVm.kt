package com.jonpeps.gamescms.ui.viewmodels.defaults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.helpers.JsonStringListHelper
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

interface IStringListToSplitItemListVm {
    fun load(directory: String,
             fileName: String,
             inputStream: InputStream
    )
}

@HiltViewModel
class StringListToSplitItemListVm@Inject constructor(
    private val viewModel: InputStreamStringListViewModel,
    private val jsonStringListHelper: JsonStringListHelper,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel(), IStringListToSplitItemListVm {
    private val _status =
        MutableStateFlow(StringListToSplitItemListData(true, null, null, "", null))
    val status: StateFlow<StringListToSplitItemListData> = _status

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
                    val result = jsonStringListHelper.splitItem(it)
                    itemNames = result.itemNames
                    fileNames = result.fileNames
                    if (itemNames.size != fileNames.size) {
                        errorMessage = ITEMS_NOT_EQUAL
                        success = false
                    }
                }?: run {
                    errorMessage = FAILED_TO_LOAD_FILE
                    success = false
                }
            } else {
                errorMessage = viewModel.status.value.errorMessage
                exception = viewModel.status.value.exception
                success = false
            }
            _status.value = StringListToSplitItemListData(
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