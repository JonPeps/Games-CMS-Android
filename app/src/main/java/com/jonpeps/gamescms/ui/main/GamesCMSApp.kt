package com.jonpeps.gamescms.ui.main

import android.app.Application
import com.jonpeps.gamescms.data.serialization.moshi.MoshiJsonBuilder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GamesCMSApp : Application() {
    init {
        MoshiJsonBuilder.build()
    }
}