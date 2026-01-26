package com.jonpeps.gamescms.data.helpers

import androidx.annotation.VisibleForTesting
import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.ui.tabletemplates.serialization.ISerializeTableTemplateHelpers

interface ITableTemplateGroupValidator {
    fun validatePrimaryKey(items: List<TableItemFinal>): Boolean
    fun validateSortKey(items: List<TableItemFinal>): Boolean
    fun validateValue(item: TableItemFinal): Boolean
    fun validateNameIsNotEmpty(name: String): Boolean
    fun validateNameIsNotDuplicate(name: String, items: List<TableItemFinal>): Boolean
    fun validateEditable(item: TableItemFinal): Boolean
    fun getParseValueErrorMsg(): String?
}

class TableTemplateGroupValidator(
    private val serializeTableTemplateHelpers: ISerializeTableTemplateHelpers):
    ITableTemplateGroupValidator {
        private var parseValueErrorMsg: String? = ""

    override fun validatePrimaryKey(items: List<TableItemFinal>): Boolean {
        return items.any { it.isPrimary }
    }

    override fun validateSortKey(items: List<TableItemFinal>): Boolean {
        return items.any { it.isSortKey }
    }

    override fun validateValue(item: TableItemFinal): Boolean {
        parseValueErrorMsg = ""
        if(serializeTableTemplateHelpers
                .validateTableTemplateValue(item.value, item.dataType)) {
            return true
        } else {
            parseValueErrorMsg = getParseValueErrorMsg(item.dataType)
            return false
        }
    }

    override fun validateNameIsNotEmpty(name: String): Boolean {
        return name.isNotEmpty()
    }

    override fun validateNameIsNotDuplicate(name: String, items: List<TableItemFinal>): Boolean {
        return items.none { it.name == name }
    }

    override fun validateEditable(item: TableItemFinal): Boolean {
        return item.editable && item.value.isNotEmpty()
    }

    override fun getParseValueErrorMsg(): String? = parseValueErrorMsg

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun getParseValueErrorMsg(type: ItemType): String {
        return "Value is not a $type"
    }
}