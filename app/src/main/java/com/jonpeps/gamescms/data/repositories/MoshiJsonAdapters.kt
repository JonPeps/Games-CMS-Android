package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.serialization.moshi.MoshiJsonBuilder
import com.squareup.moshi.JsonAdapter

class StringListMoshiJsonAdapter : MoshiJsonAdapter<StringListMoshi> {
    override fun getJsonAdapter(): JsonAdapter<StringListMoshi> =
        MoshiJsonBuilder.build().adapter(StringListMoshi::class.java)
}

