package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.base.MoshiJsonAdapter
import com.jonpeps.gamescms.data.serialization.moshi.MoshiJsonBuilder
import com.squareup.moshi.JsonAdapter

interface IMoshiJsonAdapterFactory {
    fun jsonStringList(): MoshiJsonAdapter<StringListMoshi>
    fun jsonTableTemplateItemList(): MoshiJsonAdapter<TableTemplateItemListMoshi>
    fun jsonTableTemplateDetailsList(): MoshiJsonAdapter<TableTemplateDetailsListMoshi>
}

class MoshiJsonAdapterFactoryImpl : IMoshiJsonAdapterFactory {
    override fun jsonStringList() = object : MoshiJsonAdapter<StringListMoshi> {
        override fun getJsonAdapter(): JsonAdapter<StringListMoshi> =
            MoshiJsonBuilder.build().adapter(StringListMoshi::class.java)
    }

    override fun jsonTableTemplateItemList() = object : MoshiJsonAdapter<TableTemplateItemListMoshi> {
        override fun getJsonAdapter(): JsonAdapter<TableTemplateItemListMoshi> =
            MoshiJsonBuilder.build().adapter(TableTemplateItemListMoshi::class.java)
        }

    override fun jsonTableTemplateDetailsList() = object : MoshiJsonAdapter<TableTemplateDetailsListMoshi> {
        override fun getJsonAdapter(): JsonAdapter<TableTemplateDetailsListMoshi> =
            MoshiJsonBuilder.build().adapter(TableTemplateDetailsListMoshi::class.java)
    }
}