package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
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

//////////////////////////////////////////////////////////////////////////////////////

interface IStringListMoshiJsonCache : IBasicStringGenericItemCache<StringListMoshi>

class StringListMoshiJsonCache@Inject constructor()
    : BasicStringGenericItemCache<StringListMoshi>(), IStringListMoshiJsonCache {
    override val cache: MutableMap<String, StringListMoshi> = mutableMapOf()

    override fun getDefaultItem(): StringListMoshi = StringListMoshi(items = arrayListOf())
}