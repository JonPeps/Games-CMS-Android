package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.StringListData
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface IStringListMoshiSerialization : IBaseMoshiSerialization<StringListData>

class StringListMoshiSerialization
@Inject constructor(private val moshiJsonAdapterCreator: MoshiJsonAdapterCreator,
                    dispatcher: CoroutineDispatcher
) : MoshiSerialization<StringListData>(dispatcher), IStringListMoshiSerialization {
    override fun getMoshiAdapter(): JsonAdapter<StringListData> = moshiJsonAdapterCreator.getStringListJsonAdapter()
}