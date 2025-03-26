package com.jonpeps.gamescms.ui.createtable.viewmodels

import com.jonpeps.gamescms.data.ItemType
import com.jonpeps.gamescms.data.TableItem
import com.jonpeps.gamescms.ui.createtable.viewmodels.data.CreateTableTemplateErrorType
import javax.inject.Inject

interface ICreateTableTemplateViewModel {
    fun populate(item: TableItem)
    fun setRowName(name: String)
    fun setItemType(type: ItemType)
    fun setDefaultValue(value: String)
    fun setPrimary(isPrimary: Boolean)
    fun setSortKey(isSortKey: Boolean)
    fun setIsEditable(editable: Boolean)
    fun getItem(): TableItem
}

class CreateTableTemplateViewModel@Inject constructor(createTableTemplateBridgeVm: ICreateTableTemplateBridgeVm)
    : BaseCreateTableTemplateVm<CreateTableTemplateErrorType>(createTableTemplateBridgeVm), ICreateTableTemplateViewModel {

    private var item = TableItem()

    override fun populate(item: TableItem) {
        mutableErrors.value.clear()
        this.item = item
        setRowName(item.name)
        setItemType(item.dataType)
        setDefaultValue(item.value)
        setPrimary(item.isPrimary)
        setSortKey(item.isSortKey)
        createTableTemplateBridgeVm.update(item)
    }

    override fun setRowName(name: String) {
        if (name.isEmpty()) {
            addError(CreateTableTemplateErrorType.ROW_NAME_EMPTY)
        } else {
            removeError(CreateTableTemplateErrorType.ROW_NAME_EMPTY)
            item.name = name
            createTableTemplateBridgeVm.update(item)
        }
    }

    override fun setItemType(type: ItemType) {
        item.dataType = type
    }

    override fun setDefaultValue(value: String) {
        if (value.isEmpty() && !item.editable) {
            addError(CreateTableTemplateErrorType.NO_VALUE_WITH_NOT_EDITABLE)
        } else {
            removeError(CreateTableTemplateErrorType.NO_VALUE_WITH_NOT_EDITABLE)
            item.value = value
        }
    }

    override fun setPrimary(isPrimary: Boolean) {
        item.isPrimary = isPrimary
    }

    override fun setSortKey(isSortKey: Boolean) {
        item.isSortKey = isSortKey
    }

    override fun setIsEditable(editable: Boolean) {
        item.editable = editable
    }

    override fun getItem(): TableItem = item
}