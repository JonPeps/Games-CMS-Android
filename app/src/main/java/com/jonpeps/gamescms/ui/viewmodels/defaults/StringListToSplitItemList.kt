package com.jonpeps.gamescms.ui.viewmodels.defaults

import com.jonpeps.gamescms.data.helpers.JsonStringListHelper
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringList
import java.io.InputStream
import javax.inject.Inject

interface IStringListToSplitItemList {
    suspend fun loadSuspend(inputStream: InputStream, directory: String, fileName: String)
}

class StringListToSplitItemList@Inject constructor(
    private val inputStreamStringList: InputStreamStringList,
    private val jsonStringListHelper: JsonStringListHelper
) : IStringListToSplitItemList {
    lateinit var status: StringListToSplitItemListData
    override suspend fun loadSuspend(inputStream: InputStream, directory: String, fileName: String) {
        inputStreamStringList.processSuspend(inputStream, directory, fileName)

        var success = true
        var itemNames: List<String> = emptyList()
        var fileNames: List<String> = emptyList()
        var errorMessage = ""
        var exception: Exception?  = null

        if (inputStreamStringList.status.success) {
            inputStreamStringList.status.item?.let {
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
            errorMessage = inputStreamStringList.status.errorMessage
            exception = inputStreamStringList.status.exception
            success = false
        }
        status = StringListToSplitItemListData(
            success,
            itemNames,
            fileNames,
            errorMessage,
            exception)
    }

    companion object {
        const val FAILED_TO_LOAD_FILE = "Failed to load string list from assets!"
        const val ITEMS_NOT_EQUAL = "Names and file names not equal!"
    }
}