package com.jonpeps.gamescms.ui.createtable.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.ui.createtable.helpers.ITableTemplateGroupVmRepoHelper
import com.jonpeps.gamescms.ui.createtable.viewmodels.factories.TableTemplateGroupViewModelFactory
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter

data class TableTemplateStatus(
    val success: Boolean,
    val items: ArrayList<TableTemplateItemMoshi>,
    val currentIndex: Int,
    val message: String?,
    val ex: Exception?)

interface ITableTemplateGroupViewModel {
    fun load(name: String)
    fun save(name: String)

    fun setRowName(name: String)
    fun setItemType(type: ItemType)
    fun setDefaultValue(value: String)
    fun setPrimary(isPrimary: Boolean)
    fun setSortKey(isSort: Boolean)
    fun setIsEditable(editable: Boolean)

    fun addPage()
    fun removePage()
    fun nextPage()
    fun previousPage()
    fun pageCount(): Int
    fun getCurrentPage(): TableTemplateItemMoshi
}

@HiltViewModel(assistedFactory = TableTemplateGroupViewModelFactory.ITableTemplateGroupViewModelFactory::class)
class TableTemplateGroupViewModel
@AssistedInject constructor(
    @Assisted private val tableTemplateFilesPath: String,
    private val tableTemplateRepository: ITableTemplateFileRepository,
    private val tableTemplateGroupVmRepoHelper: ITableTemplateGroupVmRepoHelper,
    private val coroutineDispatcher: CoroutineDispatcher)
    : ViewModel(), ITableTemplateGroupViewModel {

    private val _status = MutableStateFlow(TableTemplateStatus(true, arrayListOf(),0, "", null))
    val status: StateFlow<TableTemplateStatus> = _status

    private val _duplicateName = MutableStateFlow(false)
    val duplicateName: StateFlow<Boolean> = _duplicateName

    private val _noPrimaryKeyFound = MutableStateFlow(false)
    val noPrimaryKeyFound: StateFlow<Boolean> = _noPrimaryKeyFound

    private val _noSortKeyFound = MutableStateFlow(false)
    val noSortKeyFound: StateFlow<Boolean> = _noSortKeyFound

    private var _noValueWithNotEditable = MutableStateFlow(false)
    val noValueWithNotEditable: StateFlow<Boolean> = _noValueWithNotEditable

    private var _rowNameEmpty = MutableStateFlow(true)
    val rowNameEmpty: StateFlow<Boolean> = _rowNameEmpty

    private val items = arrayListOf<TableTemplateItemMoshi>()
    private var index = 0
    private var templateName = ""
    private var exception: Exception? = null

    override fun load(name: String) {
        viewModelScope.launch(coroutineDispatcher) {
            var success = true
            var errorMessage = ""
            if (initRepositoryForLoading(name)) {
                if (tableTemplateRepository.load()) {
                    val tableListItem = tableTemplateRepository.getItem()
                    tableListItem?.let {
                        templateName = it.templateName
                        items.clear()
                        items.addAll(it.items)
                        index = 0
                    }?:run {
                        success = false
                        errorMessage = tableTemplateRepository.getErrorMsg()
                    }
                } else {
                    errorMessage = FAILED_IO + getAbsolutePathName(tableTemplateFilesPath, name)
                }
             } else {
                success = false
            }
            _status.value = TableTemplateStatus(success, items, index, errorMessage, exception)
        }
    }

    override fun save(name: String) {
        viewModelScope.launch(coroutineDispatcher) {
            var success = false
            var errorMessage = ""
            if (initRepositoryForSaving(name)) {
                val tableItemList = TableTemplateItemListMoshi(name, items)
                if (tableTemplateRepository.save(tableItemList)) {
                    success = true
                } else {
                    errorMessage = tableTemplateRepository.getErrorMsg()
                }
            } else {
                errorMessage = FAILED_IO + getAbsolutePathName(tableTemplateFilesPath, name)
            }
            _status.value = TableTemplateStatus(success, items, index, errorMessage, exception)
        }
    }

    override fun setRowName(name: String) {
        if (name.isEmpty()) {
            _rowNameEmpty.value = true
            _duplicateName.value = false
        } else {
            items[index].name = name
            _rowNameEmpty.value = false
            items.forEach {
                if (it.name == name) {
                    _duplicateName.value = true
                    return
                }
            }
            _duplicateName.value = false
        }
    }

    override fun setItemType(type: ItemType) {
        items[index].dataType = type
    }

    override fun setDefaultValue(value: String) {
        _noValueWithNotEditable.value = value.isEmpty() && !items[index].editable
        items[index].value = value
    }

    override fun setPrimary(isPrimary: Boolean) {
        items[index].isPrimary = isPrimary
        if (isPrimary) {
            _noPrimaryKeyFound.value = false
            return
        }
        items.forEach {
            if (it.isPrimary) {
                _noPrimaryKeyFound.value = false
                return
            }
        }
        _noPrimaryKeyFound.value = true
    }

    override fun setSortKey(isSort: Boolean) {
        items[index].isSortKey = isSort
        if (isSort) {
            _noSortKeyFound.value = false
            return
        }
        items.forEach {
            if (it.isSortKey) {
                _noSortKeyFound.value = false
                return
            }
        }
        _noSortKeyFound.value = true
    }

    override fun setIsEditable(editable: Boolean) {
        items[index].editable = editable
        _noValueWithNotEditable.value = items[index].value.isEmpty() && !editable
    }

    override fun addPage() {
        if (items.size >= 1) {
            index++
        }
        items.add(index, TableTemplateItemMoshi())
        _status.value = TableTemplateStatus(true, items, index, "", null)
    }

    override fun removePage() {
        if (decrementPage()) {
            items.removeAt(index + 1)
            _status.value = TableTemplateStatus(true, items, index, "", null)
        }
    }

    override fun nextPage() {
        if (index < items.size - 1) {
            index++
            _status.value = TableTemplateStatus(true, items, index, "", null)
        }
    }

    override fun previousPage() {
        if (decrementPage()) {
            _status.value = TableTemplateStatus(true, items, index, "", null)
        }
    }

    override fun pageCount() = items.size

    override fun getCurrentPage(): TableTemplateItemMoshi = items[index]

    // For testing:
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun addItem(item: TableTemplateItemMoshi) {
        items.add(item)
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun clearItems() {
        items.clear()
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setTemplateName(name: String) {
        templateName = name
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setIndex(index: Int) {
        this.index = index
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getIndex() = index
    ///////////////////////////////

    private fun initRepositoryForLoading(name: String): Boolean {
        var success = true
        try {
            val absolutePath = getAbsolutePathName(tableTemplateFilesPath, name)
            val absFile = File(absolutePath)
            if (absFile.exists()) {
                tableTemplateRepository.setAbsoluteFile(absFile)
                tableTemplateRepository.setBufferReader(absFile.bufferedReader())
            } else {
                success = false
            }
        } catch (ex: Exception) {
            exception = ex
            success = false
        }
        return success
    }

    private fun initRepositoryForSaving(name: String): Boolean {
        var success = true
        try {
            val directoryFile = File(tableTemplateFilesPath)
            val file = File(name)
            val absoluteFile = File(directoryFile.name + file.name)
            tableTemplateRepository.setDirectoryFile(directoryFile)
            tableTemplateRepository.setFile(file)
            tableTemplateRepository.setFileWriter(FileWriter(absoluteFile))
        } catch (ex: Exception) {
            exception = ex
            success = false
        }
        return success
    }

    private fun decrementPage(): Boolean {
        if (index == 0) {
            return false
        }
        index--
        return true
    }

    companion object {
        const val FILE_EXTENSION = ".json"
        private const val FAILED_IO = "Failed I/O from repository: "

        fun getAbsolutePathName(path: String, fileName: String) = "$path$fileName$FILE_EXTENSION"
    }
}