package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.helpers.BasicStringGenericItemCache
import com.jonpeps.gamescms.data.helpers.IBasicStringGenericItemCache
import javax.inject.Inject

interface ITableTemplateStringMoshiJsonCache : IBasicStringGenericItemCache<TableTemplateItemMoshi>

class TableTemplateStringMoshiJsonCache@Inject constructor()
    : BasicStringGenericItemCache<TableTemplateItemMoshi>(), ITableTemplateStringMoshiJsonCache {
    override val cache: MutableMap<String, TableTemplateItemMoshi> = mutableMapOf()

    override fun getDefaultItem(): TableTemplateItemMoshi {
        return TableTemplateItemMoshi("")
    }
}