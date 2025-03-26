package com.jonpeps.gamescms.ui.createtable.viewmodels

import androidx.lifecycle.ViewModel
import com.jonpeps.gamescms.data.TableItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ICreateTableTemplateBridgeVm {
    fun setItems(items: ArrayList<TableItem>)
    fun update(item: TableItem)
    fun getItems(): ArrayList<TableItem>
}

class CreateTableTemplateBridgeVm: ViewModel(), ICreateTableTemplateBridgeVm {
    private var _items = MutableStateFlow(ArrayList<TableItem>())
    val items: StateFlow<ArrayList<TableItem>> = _items

    override fun setItems(items: ArrayList<TableItem>) {
        _items.value = items
    }

    override fun update(item: TableItem) {
        _items.value.forEach {
            if (it.id == item.id) {
                it.name = item.name
                it.dataType = item.dataType
                it.isPrimary = item.isPrimary
                it.value = item.value
                it.editable = item.editable
            }
        }
    }

    override fun getItems(): ArrayList<TableItem> = _items.value
}