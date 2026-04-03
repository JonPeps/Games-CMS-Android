package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.helpers.BasicStringGenericItemCache
import com.jonpeps.gamescms.data.helpers.IBasicStringGenericItemCache
import javax.inject.Inject

interface IStringListMoshiJsonCache
    : IBasicStringGenericItemCache<StringListMoshi>

interface ITableTemplateStringMoshiJsonCache
    : IBasicStringGenericItemCache<TableTemplateItemListMoshi>

interface IMoshiJsonCachesFactory {
    fun stringMoshiJson()
        : IStringListMoshiJsonCache
    fun tableTemplateStringMoshiJson()
        : ITableTemplateStringMoshiJsonCache
}

class MoshiJsonCachesFactoryImpl @Inject constructor(): IMoshiJsonCachesFactory {
    override fun stringMoshiJson(): IStringListMoshiJsonCache {
        return object : BasicStringGenericItemCache<StringListMoshi>(), IStringListMoshiJsonCache {
            override val cache: MutableMap<String, StringListMoshi> = mutableMapOf()
            override fun getDefaultItem(): StringListMoshi = StringListMoshi(listOf())
        }
    }

    override fun tableTemplateStringMoshiJson(): ITableTemplateStringMoshiJsonCache {
        return object : BasicStringGenericItemCache<TableTemplateItemListMoshi>(), ITableTemplateStringMoshiJsonCache {
            override val cache: MutableMap<String, TableTemplateItemListMoshi> = mutableMapOf()
            override fun getDefaultItem(): TableTemplateItemListMoshi = TableTemplateItemListMoshi("")
        }
    }
}