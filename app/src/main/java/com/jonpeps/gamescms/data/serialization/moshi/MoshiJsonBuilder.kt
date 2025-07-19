package com.jonpeps.gamescms.data.serialization.moshi

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiJsonBuilder {
    companion object {
        fun build(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }
}