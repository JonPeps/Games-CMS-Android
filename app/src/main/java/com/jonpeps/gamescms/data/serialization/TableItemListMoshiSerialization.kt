package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.core.IStringSerialization
import com.jonpeps.gamescms.data.core.MoshiJsonAdapterCreator
import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TableItemListMoshiSerialization
@Inject constructor(private val moshiJsonAdapterCreator: MoshiJsonAdapterCreator,
                    serializeString: IStringSerialization,
                    dispatcher: CoroutineDispatcher
) : MoshiSerialization<TableItemList>(serializeString, dispatcher) {
    override fun getMoshiAdapter(): JsonAdapter<TableItemList> = moshiJsonAdapterCreator.getTableItemTemplateJsonAdapter()
}