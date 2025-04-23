package com.jonpeps.gamescms.data.dataclasses.mappers

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.dataclasses.room.TableItemRoom

interface ITableItemFinalMapper {
    fun fromTableTemplateMoshi(tableTemplateItemMoshi: TableTemplateItemMoshi): TableItemFinal
    fun toTableTemplateItemMoshi(tableItemFinal: TableItemFinal): TableTemplateItemMoshi
    fun toTableTemplateItemListMoshi(templateName: String, tableItemFinal: List<TableItemFinal>): TableTemplateItemListMoshi
    fun fromTableItemRoom(tableItemRoom: TableItemRoom): TableItemFinal
    fun fromTableTemplatesMoshi(tableTemplateItemMoshi: List<TableTemplateItemMoshi>): List<TableItemFinal>
    fun fromTableItemsRoom(tableItemRoom: List<TableItemRoom>): List<TableItemFinal>
    fun getDefaultTableItemFinal(): TableItemFinal
}

class TableItemFinalMapper : ITableItemFinalMapper {
    override fun fromTableTemplateMoshi(tableTemplateItemMoshi: TableTemplateItemMoshi): TableItemFinal {
        return TableItemFinal(tableTemplateItemMoshi.name,
            tableTemplateItemMoshi.dataType,
            tableTemplateItemMoshi.isPrimary,
            tableTemplateItemMoshi.value,
            tableTemplateItemMoshi.editable,
            tableTemplateItemMoshi.isSortKey)
    }

    override fun toTableTemplateItemMoshi(tableItemFinal: TableItemFinal): TableTemplateItemMoshi {
        return TableTemplateItemMoshi(
            tableItemFinal.name,
            tableItemFinal.dataType,
            tableItemFinal.isPrimary,
            tableItemFinal.value,
            tableItemFinal.editable,
            tableItemFinal.isSortKey)
    }

    override fun toTableTemplateItemListMoshi(
        templateName: String,
        tableItemFinal: List<TableItemFinal>
    ): TableTemplateItemListMoshi {
        return TableTemplateItemListMoshi(templateName, tableItemFinal.map { toTableTemplateItemMoshi(it) })
    }

    override fun fromTableItemRoom(tableItemRoom: TableItemRoom): TableItemFinal {
        return TableItemFinal(tableItemRoom.name,
            tableItemRoom.dataType,
            tableItemRoom.isPrimary,
            tableItemRoom.value,
            tableItemRoom.editable,
            tableItemRoom.isSortKey)
    }

    override fun fromTableTemplatesMoshi(tableTemplateItemMoshi: List<TableTemplateItemMoshi>):
            List<TableItemFinal> {
            return tableTemplateItemMoshi.map { fromTableTemplateMoshi(it) }
    }

    override fun fromTableItemsRoom(tableItemRoom: List<TableItemRoom>): List<TableItemFinal> {
        return tableItemRoom.map { fromTableItemRoom(it) }
    }

    override fun getDefaultTableItemFinal(): TableItemFinal {
        return TableItemFinal("", ItemType.STRING, false, "", true)
    }
}