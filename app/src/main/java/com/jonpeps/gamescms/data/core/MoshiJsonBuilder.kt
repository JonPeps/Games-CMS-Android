package com.jonpeps.gamescms.data.core

import com.squareup.moshi.Moshi

class MoshiJsonBuilder {
    companion object {
        fun build() = Moshi.Builder().build()
    }
}