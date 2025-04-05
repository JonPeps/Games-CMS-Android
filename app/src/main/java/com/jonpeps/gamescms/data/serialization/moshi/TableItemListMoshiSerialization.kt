package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface ITableItemListMoshiSerialization : IBaseMoshiSerialization<TableItemList>

class TableItemListMoshiSerialization
@Inject constructor(private val moshiJsonAdapterCreator: MoshiJsonAdapterCreator,
                    dispatcher: CoroutineDispatcher
) : MoshiSerialization<TableItemList>(dispatcher),
    ITableItemListMoshiSerialization {
    override fun getMoshiAdapter(): JsonAdapter<TableItemList> = moshiJsonAdapterCreator.getTableItemTemplateJsonAdapter()
}