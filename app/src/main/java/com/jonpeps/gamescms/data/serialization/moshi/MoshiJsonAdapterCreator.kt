package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.squareup.moshi.JsonAdapter
import javax.inject.Inject

interface IMoshiJsonAdapterCreator {
    fun getTableItemTemplateJsonAdapter(): JsonAdapter<TableTemplateItemListMoshi>
    fun getStringListJsonAdapter(): JsonAdapter<StringListMoshi>
}

class MoshiJsonAdapterCreator@Inject constructor() : IMoshiJsonAdapterCreator {
    private val tableItemTemplateJsonAdapter: JsonAdapter<TableTemplateItemListMoshi>
    private val stringListJsonAdapter: JsonAdapter<StringListMoshi>

    init {
        val build = MoshiJsonBuilder.build()
        tableItemTemplateJsonAdapter = build.adapter(TableTemplateItemListMoshi::class.java)
        stringListJsonAdapter = build.adapter(StringListMoshi::class.java)
    }

    override fun getTableItemTemplateJsonAdapter(): JsonAdapter<TableTemplateItemListMoshi> = tableItemTemplateJsonAdapter
    override fun getStringListJsonAdapter(): JsonAdapter<StringListMoshi> = stringListJsonAdapter
}