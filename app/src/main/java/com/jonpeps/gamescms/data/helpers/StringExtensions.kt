package com.jonpeps.gamescms.data.helpers

import com.jonpeps.gamescms.data.DataConstants.Companion.SPACE
import com.jonpeps.gamescms.data.DataConstants.Companion.UNDER_SCORE
import com.jonpeps.gamescms.data.DataConstants.Companion.UTC_DATE_FORMAT
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun String.toCommonFilename(): String {
    return this.replace(SPACE, UNDER_SCORE).lowercase(Locale.getDefault())
}

fun String.toLocalDate(): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern(UTC_DATE_FORMAT))
    } catch (_: Exception) {
        null
    }
}