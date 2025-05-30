package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface IStringListMoshiSerialization : IBaseMoshiSerialization<StringListMoshi>

class StringListMoshiSerialization
@Inject constructor(private val moshiJsonAdapterCreator: MoshiJsonAdapterCreator,
                    dispatcher: CoroutineDispatcher
) : MoshiSerialization<StringListMoshi>(dispatcher), IStringListMoshiSerialization {
    override fun getMoshiAdapter(): JsonAdapter<StringListMoshi> = moshiJsonAdapterCreator.getStringListJsonAdapter()
}