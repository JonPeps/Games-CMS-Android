package com.jonpeps.gamescms.ui.createtable.viewmodels

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ITableTemplateSingleItemViewModel {
    fun setRowName(name: String)
    fun setItemType(type: ItemType)
    fun setDefaultValue(value: String)
    fun setPrimary(isPrimary: Boolean)
    fun setSortKey(isSort: Boolean)
    fun setIsEditable(editable: Boolean)
    fun getItem(): TableItem
}

interface IGlobalWatchCoreValuesChangedListener {
    fun onNameChanged(name: String)
    fun isPrimaryChanged(isPrimary: Boolean)
    fun isSortKeyChanged(isSort: Boolean)
}

@Suppress("UNCHECKED_CAST")
class TableTemplateSingleItemViewModel(private val listener: IGlobalWatchCoreValuesChangedListener)
: ViewModel(), ITableTemplateSingleItemViewModel {
    companion object {
        fun provideFactory(
            listener: IGlobalWatchCoreValuesChangedListener,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null,
            ): AbstractSavedStateViewModelFactory =
                object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                    override fun <T : ViewModel> create(
                        key: String,
                        modelClass: Class<T>,
                        handle: SavedStateHandle
                    ): T {
                        return TableTemplateSingleItemViewModel(listener) as T
                    }
        }
    }

    private var item = TableItem()

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

    override fun getItem(): TableItem = item
}