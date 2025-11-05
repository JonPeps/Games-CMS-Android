package com.jonpeps.gamescms.ui.main.builders.core

import androidx.compose.runtime.Composable
import com.jonpeps.gamescms.ui.applevel.CustomColours
import software.amazon.awssdk.core.interceptor.Context

class TableTemplateAssetFilesToStorageBuilder private constructor() {
    data class Builder(val context: Context, val customColours: CustomColours) {
        @Composable
        fun Build() {

        }
    }
}