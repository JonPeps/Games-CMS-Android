package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.StringListData
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.squareup.moshi.JsonAdapter

interface IMoshiJsonAdapterCreator {
    fun getTableItemTemplateJsonAdapter(): JsonAdapter<TableTemplateItemListMoshi>
    fun getStringListJsonAdapter(): JsonAdapter<StringListData>
}

class MoshiJsonAdapterCreator : IMoshiJsonAdapterCreator {
    private val tableItemTemplateJsonAdapter: JsonAdapter<TableTemplateItemListMoshi>
    private val stringListJsonAdapter: JsonAdapter<StringListData>

    init {
        val build = MoshiJsonBuilder.build()
        tableItemTemplateJsonAdapter = build.adapter(TableTemplateItemListMoshi::class.java)
        stringListJsonAdapter = build.adapter(StringListData::class.java)
    }

    override fun getTableItemTemplateJsonAdapter(): JsonAdapter<TableTemplateItemListMoshi> = tableItemTemplateJsonAdapter
    override fun getStringListJsonAdapter(): JsonAdapter<StringListData> = stringListJsonAdapter
}
