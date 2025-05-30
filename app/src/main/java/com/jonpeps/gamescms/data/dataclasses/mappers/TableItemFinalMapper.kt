package com.jonpeps.gamescms.data.dataclasses.mappers

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi

class TableItemFinalMapper {
    companion object  {
        fun fromTableTemplateMoshi(tableTemplateItemMoshi: TableTemplateItemMoshi): TableItemFinal {
            return TableItemFinal(tableTemplateItemMoshi.name,
                tableTemplateItemMoshi.dataType,
                tableTemplateItemMoshi.isPrimary,
                tableTemplateItemMoshi.value,
                tableTemplateItemMoshi.editable,
                tableTemplateItemMoshi.isSortKey)
        }

        fun toTableTemplateItemMoshi(tableItemFinal: TableItemFinal): TableTemplateItemMoshi {
            return TableTemplateItemMoshi(
                tableItemFinal.name,
                tableItemFinal.dataType,
                tableItemFinal.isPrimary,
                tableItemFinal.value,
                tableItemFinal.editable,
                tableItemFinal.isSortKey)
        }

        fun fromTableTemplateListMoshi(tableTemplateItemMoshi: List<TableTemplateItemMoshi>):
                List<TableItemFinal> {
                return tableTemplateItemMoshi.map { fromTableTemplateMoshi(it) }
        }

        fun toTableTemplateItemListMoshi(
            templateName: String,
            tableItemFinal: List<TableItemFinal>
        ): TableTemplateItemListMoshi {
            return TableTemplateItemListMoshi(templateName, tableItemFinal.map { toTableTemplateItemMoshi(it) })
        }
    }
}