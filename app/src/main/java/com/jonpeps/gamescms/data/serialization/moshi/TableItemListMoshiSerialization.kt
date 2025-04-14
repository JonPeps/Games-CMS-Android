package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher


interface ITableItemListMoshiSerialization : IBaseMoshiSerialization<TableTemplateItemListMoshi>

class TableItemListMoshiSerialization
(private val moshiJsonAdapterCreator: MoshiJsonAdapterCreator,
                    dispatcher: CoroutineDispatcher
) : MoshiSerialization<TableTemplateItemListMoshi>(dispatcher),
    ITableItemListMoshiSerialization {
    override fun getMoshiAdapter(): JsonAdapter<TableTemplateItemListMoshi> = moshiJsonAdapterCreator.getTableItemTemplateJsonAdapter()
}