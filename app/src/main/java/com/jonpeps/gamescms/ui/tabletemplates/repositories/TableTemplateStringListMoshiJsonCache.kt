package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.helpers.BasicStringGenericItemCache
import com.jonpeps.gamescms.data.helpers.IBasicStringGenericItemCache
import javax.inject.Inject

interface ITableTemplateStringListMoshiJsonCache : IBasicStringGenericItemCache<TableTemplateItemListMoshi>

class TableTemplateStringListMoshiJsonCache@Inject constructor()
    : BasicStringGenericItemCache<TableTemplateItemListMoshi>(), ITableTemplateStringListMoshiJsonCache {
    override val cache: MutableMap<String, TableTemplateItemListMoshi> = mutableMapOf()

    override fun getDefaultItem(): TableTemplateItemListMoshi = TableTemplateItemListMoshi(templateName = "")
}