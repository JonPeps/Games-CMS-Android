package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject


interface ITableItemListMoshiSerialization : IBaseMoshiSerialization<TableTemplateItemListMoshi>

class TableItemListMoshiSerialization
@Inject constructor(private val moshiJsonAdapterCreator: MoshiJsonAdapterCreator,
                    dispatcher: CoroutineDispatcher
) : MoshiSerialization<TableTemplateItemListMoshi>(dispatcher),
    ITableItemListMoshiSerialization {
    override fun getMoshiAdapter(): JsonAdapter<TableTemplateItemListMoshi> = moshiJsonAdapterCreator.getTableItemTemplateJsonAdapter()
}