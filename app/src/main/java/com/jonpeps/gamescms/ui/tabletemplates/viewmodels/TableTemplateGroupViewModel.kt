package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.dataclasses.mappers.TableItemFinalMapper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.factories.TableTemplateGroupViewModelFactory
import com.jonpeps.gamescms.data.repositories.ITableTemplateFileRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TableTemplateStatus(
    val success: Boolean,
    val items: ArrayList<TableItemFinal>,
    val currentIndex: Int,
    val message: String?,
    val ex: Exception?)

interface ITableTemplateGroupViewModel {
    fun load(name: String, loadFromCacheIfExists: Boolean = true)
    fun save(name: String)
    fun new()

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
    fun getCurrentPage(): TableItemFinal

    fun hasChanges(): Boolean
    fun reset()
}

@HiltViewModel(assistedFactory = TableTemplateGroupViewModelFactory.ITableTemplateGroupViewModelFactory::class)
class TableTemplateGroupViewModel
@AssistedInject constructor(
    @Assisted private val tableTemplateFilesPath: String,
    private val tableTemplateRepository: ITableTemplateFileRepository,
    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    private val tableTemplateGroupVmChangesCache: ITableTemplateGroupVmChangesCache,
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

    private var _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    private var items = arrayListOf<TableItemFinal>()
    private var index = 0
    private var templateName = ""
    private var exception: Exception? = null

    override fun load(name: String, loadFromCacheIfExists: Boolean) {
        viewModelScope.launch(coroutineDispatcher) {
            _isProcessing.value = true
            templateName = name
            exception = null
            var errorMessage = ""
            var success = true
            if (loadFromCacheIfExists && tableTemplateGroupVmChangesCache.isPopulated()) {
                items = ArrayList(tableTemplateGroupVmChangesCache.get(templateName))
            } else {
                try {
                    initReadFiles(name)
                    if (tableTemplateRepository.load(templateName)) {
                        val tableListItem = tableTemplateRepository.getItem(templateName)
                        tableListItem?.let {
                            templateName = it.templateName
                            items.clear()
                            items.addAll(TableItemFinalMapper.fromTableTemplateListMoshi(it.items))
                            index = 0
                        }?:run {
                            success = false
                            errorMessage = JSON_ITEM_TO_LOAD_IS_NULL + name
                        }
                    } else {
                        success = false
                        errorMessage = tableTemplateRepository.getErrorMsg()
                    }
                } catch (ex: Exception) {
                    exception = ex
                    errorMessage = ex.message.toString()
                    success = false
                }
                if (success) {
                    tableTemplateGroupVmChangesCache.set(templateName, items)
                }
            }
            _status.value = TableTemplateStatus(success, items, index, errorMessage, exception)
        }
    }

    override fun save(name: String) {
        viewModelScope.launch(coroutineDispatcher) {
            _isProcessing.value = true
            templateName = name
            exception = null
            var success = false
            var errorMessage = ""
            try {
                initWriteFiles(name)
                val toSave = tableTemplateRepository.getItem(templateName)
                toSave?.let {
                    if (tableTemplateRepository.save(templateName, it)) {
                        success = true
                    } else {
                        errorMessage = tableTemplateRepository.getErrorMsg()
                    }
                }?:run {
                    success = false
                    errorMessage = JSON_ITEM_TO_SAVE_IS_NULL + name
                }
            } catch (ex: Exception) {
                exception = ex
                errorMessage = ex.message.toString()
                success = false
            }
            if (success) {
                tableTemplateGroupVmChangesCache.updateCurrent(templateName, items)
            }
            _status.value = TableTemplateStatus(success, items, index, errorMessage, exception)
        }
    }

    override fun new() {
        _isProcessing.value = true
        items.clear()
        items.add(TableItemFinal(name = "", dataType = ItemType.STRING,
            isPrimary = false, value = "", editable = true, isSortKey = false)
        )
        _status.value = TableTemplateStatus(true, items, index, "", null)
    }

    override fun setRowName(name: String) {
        if (name.isEmpty()) {
            _rowNameEmpty.value = true
            _duplicateName.value = false
        } else {
            items.forEach {
                if (it.name == name) {
                    _duplicateName.value = true
                }
            }
            items[index].name = name
            _rowNameEmpty.value = false
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
        } else {
            items.forEach {
                if (it.isPrimary) {
                    _noPrimaryKeyFound.value = false
                    return
                }
            }
            _noPrimaryKeyFound.value = true
        }
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
        if (items.isNotEmpty()) {
            index++
        }
        items.add(index, TableItemFinal(name = "", dataType = ItemType.STRING,
            isPrimary = false, value = "", editable = true, isSortKey = false))
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

    override fun getCurrentPage(): TableItemFinal = items[index]

    override fun hasChanges(): Boolean = tableTemplateGroupVmChangesCache.hasChanges(templateName)

    override fun reset() {
        tableTemplateGroupVmChangesCache.reset(templateName)
        items = ArrayList(tableTemplateGroupVmChangesCache.get(templateName))
        _status.value = TableTemplateStatus(true, items, index, "", null)
    }

    // For testing:
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun addItem(item: TableItemFinal) {
        items.add(item)
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun clearItems() {
        items.clear()
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun setIndex(index: Int) {
        this.index = index
    }
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getIndex() = index
    ///////////////////////////////

    private fun decrementPage(): Boolean {
        if (index == 0) {
            return false
        }
        index--
        return true
    }

    private fun initReadFiles(name: String) {
        tableTemplateRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(tableTemplateFilesPath, name))
        tableTemplateRepository.setBufferReader(
            commonSerializationRepoHelper.getBufferReader(tableTemplateFilesPath, name))
    }

    private fun initWriteFiles(name: String) {
        tableTemplateRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(tableTemplateFilesPath, name))
        tableTemplateRepository.setFile(commonSerializationRepoHelper.getMainFile(name))
        tableTemplateRepository.setDirectoryFile(
            commonSerializationRepoHelper.getDirectoryFile(tableTemplateFilesPath))
        tableTemplateRepository.setFileWriter(
            commonSerializationRepoHelper.getFileWriter(tableTemplateFilesPath, name))
        tableTemplateRepository
            .setItem(templateName, TableItemFinalMapper.toTableTemplateItemListMoshi(name, items))
    }

    companion object {
        const val JSON_ITEM_TO_SAVE_IS_NULL = "Json item to save is null for table template: "
        const val JSON_ITEM_TO_LOAD_IS_NULL = "Json item to load is null for table template: "
    }
}