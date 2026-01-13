package com.jonpeps.gamescms.data.helpers

import com.jonpeps.gamescms.data.DataConstants.Companion.UNDER_SCORE
import java.util.Locale

fun String.toCommonFilename(): String {
    return this.replace(" ", UNDER_SCORE).lowercase(Locale.getDefault())
}