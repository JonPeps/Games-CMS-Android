package com.jonpeps.gamescms.ui.createtable.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.TableItem
import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

data class TableTemplateStatus(
    val success: Boolean,
    val items: ArrayList<TableItem>,
    val currentIndex: Int,
    val message: String?,
    val ex: Exception?)

interface ITableTemplateGroupViewModel : IGlobalWatchCoreValuesChangedListener {
    fun load(absoluteFile: File, bufferedReader: BufferedReader)
    fun save(filePath: File, mainFile: File, writer: FileWriter)
    fun addPage()
    fun removePage()
    fun nextPage()
    fun previousPage()
    fun pageCount(): Int
    fun getCurrentPage(): TableItem
}

class TableTemplateGroupViewModel
@Inject constructor(private val tableTemplateRepository: ITableTemplateRepository,
                    private val coroutineDispatcher: CoroutineDispatcher)
    : ViewModel(), ITableTemplateGroupViewModel {

    private val _status = MutableStateFlow(TableTemplateStatus(true, arrayListOf(),0, null, null))
    val status: StateFlow<TableTemplateStatus> = _status

    private val _duplicateName = MutableStateFlow(false)
    val duplicateName: StateFlow<Boolean> = _duplicateName

    private val _noPrimaryKeyFound = MutableStateFlow(false)
    val noPrimaryKeyFound: StateFlow<Boolean> = _noPrimaryKeyFound

    private val _noSortKeyFound = MutableStateFlow(false)
    val noSortKeyFound: StateFlow<Boolean> = _noSortKeyFound

    private val items = arrayListOf<TableItem>()
    private var index = 0

    override fun load(absoluteFile: File, bufferedReader: BufferedReader) {
        viewModelScope.launch(coroutineDispatcher) {
            tableTemplateRepository.setAbsolutePath(absoluteFile)
            tableTemplateRepository.setBufferReader(bufferedReader)
            if (tableTemplateRepository.load()) {
                var success = true
                var errorMessage = ""
                val tableListItem = tableTemplateRepository.getItem()
                tableListItem?.let {
                    items.clear()
                    items.addAll(it.items)
                    index = 0
                }?:run {
                    success = false
                    errorMessage = FAILED_TO_LOAD_TEMPLATE + absoluteFile.name
                }
                _status.value = TableTemplateStatus(success, items, index, errorMessage, null)
            } else {
                _status.value = TableTemplateStatus(false, items, index, tableTemplateRepository.getErrorMsg(), null)
            }
        }
    }

    override fun save(filePath: File, mainFile: File, writer: FileWriter) {
        viewModelScope.launch(coroutineDispatcher) {
            tableTemplateRepository.setFilePath(filePath)
            tableTemplateRepository.setFile(mainFile)
            tableTemplateRepository.setFileWriter(writer)
            val fileName = mainFile.name.replace(FILE_EXTENSION, "")
            if (tableTemplateRepository.save(TableItemList(fileName, items))) {
                _status.value = TableTemplateStatus(true, items, index, null, null)
            } else {
                _status.value = TableTemplateStatus(false, items, index,
                    FAILED_TO_SAVE_TEMPLATE + filePath.name + mainFile.name, null)
            }
        }
    }

    override fun addPage() {
        val item = TableItem()
        index++
        items.add(index, item)
        _status.value = TableTemplateStatus(true, items, index, null, null)
    }

    override fun removePage() {
        if (decrementPage()) {
            items.removeAt(index + 1)
            _status.value = TableTemplateStatus(true, items, index, null, null)
        }
    }

    override fun nextPage() {
        if (index == _status.value.items.size - 1) return
        index++
        _status.value = TableTemplateStatus(true, items, index, null, null)
    }

    override fun previousPage() {
        decrementPage()
    }

    override fun pageCount() = _status.value.items.size

    override fun getCurrentPage(): TableItem = items[index]

    override fun onNameChanged(name: String) {
        if (name.isEmpty()) return
        _status.value.items.forEach {
            if (it.name == name) {
                _duplicateName.value = true
                return
            }
        }
        _duplicateName.value = false
    }

    override fun isPrimaryChanged(isPrimary: Boolean) {
        _status.value.items.forEach {
            if (it.isPrimary) {
                _noPrimaryKeyFound.value = false
                return
            }
        }
        _noPrimaryKeyFound.value = true
    }

    override fun isSortKeyChanged(isSort: Boolean) {
        _status.value.items.forEach {
            if (it.isSortKey) {
                _noSortKeyFound.value = false
                return
            }
        }
        _noSortKeyFound.value = true
    }

    private fun decrementPage(): Boolean {
        if (index == 0) return false
        index--
        return true
    }

    companion object {
        const val FILE_EXTENSION = ".json"
        const val FAILED_TO_LOAD_TEMPLATE = "Failed to load template: "
        const val FAILED_TO_SAVE_TEMPLATE = "Failed to save template: "

        fun getAbsolutePathName(path: String, fileName: String) = "$path$fileName$FILE_EXTENSION"
    }
}