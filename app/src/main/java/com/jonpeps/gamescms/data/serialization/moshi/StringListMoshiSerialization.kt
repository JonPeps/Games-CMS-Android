package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.StringListData
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface IStringListMoshiSerialization : IBaseMoshiSerialization<TableTemplateItemListMoshi>

class StringListMoshiSerialization
@Inject constructor(private val moshiJsonAdapterCreator: MoshiJsonAdapterCreator,
                    dispatcher: CoroutineDispatcher
) : MoshiSerialization<TableTemplateItemListMoshi>(dispatcher), IStringListMoshiSerialization {
    override fun getMoshiAdapter(): JsonAdapter<TableTemplateItemListMoshi> = moshiJsonAdapterCreator.getStringListJsonAdapter()
}