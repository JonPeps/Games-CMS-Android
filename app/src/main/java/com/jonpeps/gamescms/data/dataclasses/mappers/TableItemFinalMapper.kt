package com.jonpeps.gamescms.data.dataclasses.mappers

import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.dataclasses.room.TableItemRoom

interface ITableItemFinalMapper {
    fun fromTableTemplateMoshi(tableTemplateItemMoshi: TableTemplateItemMoshi): TableItemFinal
    fun fromTableItemRoom(tableItemRoom: TableItemRoom): TableItemFinal
    fun fromTableTemplatesMoshi(tableTemplateItemMoshi: List<TableTemplateItemMoshi>): List<TableItemFinal>
    fun fromTableItemsRoom(tableItemRoom: List<TableItemRoom>): List<TableItemFinal>
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
}