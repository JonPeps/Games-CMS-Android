package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.JsonAdapterStringListMoshi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiJsonBuilder {
    companion object {
        fun build(): Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(StringListMoshi::class.java)
            .build()
    }
}