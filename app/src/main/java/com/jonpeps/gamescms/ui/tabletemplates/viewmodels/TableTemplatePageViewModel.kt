package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.helpers.ITableTemplateGroupValidator
import com.jonpeps.gamescms.ui.tabletemplates.serialization.ISerializeTableTemplateHelpers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface ITableTemplatePageViewModel {
    fun setItems(items: List<TableItemFinal>)
    fun setIndex(index: Int)
    fun setRowName(name: String)
    fun setItemType(type: ItemType)
    fun setPrimary(isPrimary: Boolean)
    fun setSortKey(isSort: Boolean)
    fun clearItems()
    fun getCurrentPage(): TableItemFinal
    fun getParseErrorMsg(): String
}

@HiltViewModel
class TableTemplatePageViewModel@Inject constructor(
    private val validator: ITableTemplateGroupValidator,
    private val serializeTableTemplateHelpers: ISerializeTableTemplateHelpers)
    : ViewModel(), ITableTemplatePageViewModel {

    private lateinit var items: List<TableItemFinal>
    private var index: Int = 0
    private var parseValueErrorMsg = ""

    private val _isDuplicateName = MutableStateFlow(false)
    val isDuplicateName: StateFlow<Boolean> = _isDuplicateName

    private val _primaryKeyFound = MutableStateFlow(false)
    val primaryKeyFound: StateFlow<Boolean> = _primaryKeyFound

    private val _sortKeyFound = MutableStateFlow(false)
    val sortKeyFound: StateFlow<Boolean> = _sortKeyFound

    private var _rowNameIsNotEmpty = MutableStateFlow(false)
    val rowNameIsNotEmpty: StateFlow<Boolean> = _rowNameIsNotEmpty

    private var _parseValueError = MutableStateFlow(false)
    val parseValueError: StateFlow<Boolean> = _parseValueError

    override fun setItems(items: List<TableItemFinal>) {
        this.items = items
    }

    override fun setIndex(index: Int) {
        this.index = index
    }

    override fun setRowName(name: String) {
        _rowNameIsNotEmpty.value = validator
            .validateNameIsNotEmpty(name)
        _isDuplicateName.value = !validator.validateNameIsNotDuplicate(name, items)
        items[index].name = name
    }

    override fun setItemType(type: ItemType) {
        items[index].dataType = type
        determineIfParseValueError(type)
    }

    override fun setPrimary(isPrimary: Boolean) {
        items[index].isPrimary = isPrimary
        if (isPrimary) {
            _primaryKeyFound.value = true
        } else {
            items.forEach {
                if (it.isPrimary) {
                    _primaryKeyFound.value = true
                    return
                }
            }
            _primaryKeyFound.value = false
        }
    }

    override fun setSortKey(isSort: Boolean) {
        items[index].isSortKey = isSort
        if (isSort) {
            _sortKeyFound.value = true
            return
        }
        items.forEach {
            if (it.isSortKey) {
                _sortKeyFound.value = true
                return
            }
        }
        _sortKeyFound.value = false
    }

    override fun clearItems() {
        items = listOf()
        index = 0
    }

    override fun getCurrentPage() = items[index]

    override fun getParseErrorMsg(): String = parseValueErrorMsg

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun determineIfParseValueError(type: ItemType) {
        _parseValueError.value = !serializeTableTemplateHelpers
            .validateTableTemplateValue(items[index].value, type)
        parseValueErrorMsg = if (_parseValueError.value) {
            getParseValueErrorMsg(type)
        } else {
            ""
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getParseValueErrorMsg(type: ItemType): String {
        return "Value is not a $type"
    }
}
