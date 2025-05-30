package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.helpers.BasicStringGenericItemCache
import com.jonpeps.gamescms.data.helpers.IBasicStringGenericItemCache
import javax.inject.Inject

interface ITableTemplateStringMoshiJsonCache : IBasicStringGenericItemCache<TableTemplateItemListMoshi>

class TableTemplateStringMoshiJsonCache@Inject constructor()
    : BasicStringGenericItemCache<TableTemplateItemListMoshi>(), ITableTemplateStringMoshiJsonCache {
    override val cache: MutableMap<String, TableTemplateItemListMoshi> = mutableMapOf()

    override fun getDefaultItem() = TableTemplateItemListMoshi("")
}