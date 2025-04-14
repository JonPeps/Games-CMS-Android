package com.jonpeps.gamescms.ui.createtable.viewmodels

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ITableTemplateSingleItem {
    fun setRowName(name: String)
    fun setItemType(type: ItemType)
    fun setDefaultValue(value: String)
    fun setPrimary(isPrimary: Boolean)
    fun setSortKey(isSort: Boolean)
    fun setIsEditable(editable: Boolean)
    fun getItem(): TableItemFinal
}

interface IGlobalWatchCoreValuesChangedListener {
    fun onNameChanged(name: String)
    fun isPrimaryChanged(isPrimary: Boolean)
    fun isSortKeyChanged(isSort: Boolean)
}

class TableTemplateSingleItem(private val listener: IGlobalWatchCoreValuesChangedListener,
                              private var item: TableItemFinal)
: ITableTemplateSingleItem {
    private var _noValueWithNotEditable = MutableStateFlow(false)
    private var _rowNameEmpty = MutableStateFlow(true)

    val noValueWithNotEditable: StateFlow<Boolean> = _noValueWithNotEditable
    val rowNameEmpty: StateFlow<Boolean> = _rowNameEmpty

    override fun setRowName(name: String) {
        if (name.isEmpty()) {
            _rowNameEmpty.value = true
        } else {
            item.name = name
            _rowNameEmpty.value = false
            listener.onNameChanged(name)
        }
    }

    override fun setItemType(type: ItemType) {
        item.dataType = type
    }

    override fun setDefaultValue(value: String) {
        _noValueWithNotEditable.value = value.isEmpty() && !item.editable
        item.value = value
    }

    override fun setPrimary(isPrimary: Boolean) {
        item.isPrimary = isPrimary
        listener.isPrimaryChanged(isPrimary)
    }

    override fun setSortKey(isSort: Boolean) {
        item.isSortKey = isSort
        listener.isSortKeyChanged(isSort)
    }

    override fun setIsEditable(editable: Boolean) {
        item.editable = editable
        _noValueWithNotEditable.value = item.value.isEmpty() && !editable
    }

    override fun getItem(): TableItemFinal = item
}