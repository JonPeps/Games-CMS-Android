package com.jonpeps.gamescms.data.core

import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.squareup.moshi.JsonAdapter

interface IMoshiJsonAdapterCreator {
    fun getTableItemTemplateJsonAdapter(): JsonAdapter<TableItemList>
}

class MoshiJsonAdapterCreator : IMoshiJsonAdapterCreator {
    private val tableItemTemplateJsonAdapter: JsonAdapter<TableItemList>

    init {
        val build = MoshiJsonBuilder.build()
        tableItemTemplateJsonAdapter = build.adapter(TableItemList::class.java)
    }

    override fun getTableItemTemplateJsonAdapter(): JsonAdapter<TableItemList> = tableItemTemplateJsonAdapter
}
