package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.StringListData
import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.squareup.moshi.JsonAdapter

interface IMoshiJsonAdapterCreator {
    fun getTableItemTemplateJsonAdapter(): JsonAdapter<TableItemList>
    fun getStringListJsonAdapter(): JsonAdapter<StringListData>
}

class MoshiJsonAdapterCreator : IMoshiJsonAdapterCreator {
    private val tableItemTemplateJsonAdapter: JsonAdapter<TableItemList>
    private val stringListJsonAdapter: JsonAdapter<StringListData>

    init {
        val build = MoshiJsonBuilder.build()
        tableItemTemplateJsonAdapter = build.adapter(TableItemList::class.java)
        stringListJsonAdapter = build.adapter(StringListData::class.java)
    }

    override fun getTableItemTemplateJsonAdapter(): JsonAdapter<TableItemList> = tableItemTemplateJsonAdapter
    override fun getStringListJsonAdapter(): JsonAdapter<StringListData> = stringListJsonAdapter
}
