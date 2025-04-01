package com.jonpeps.gamescms.data.serialization.moshi

import com.squareup.moshi.Moshi

class MoshiJsonBuilder {
    companion object {
        fun build(): Moshi = Moshi.Builder().build()
    }
}